package com.library.ServiceCatalog.services;

import com.library.ServiceCatalog.dto.*;
import com.library.ServiceCatalog.exceptions.CategoriesNotFoundException;
import com.library.ServiceCatalog.models.Book;
import com.library.ServiceCatalog.models.BookForKafka;
import com.library.ServiceCatalog.repositories.BookForKafkaRepository;
import com.library.ServiceCatalog.repositories.BookRepository;
import com.library.ServiceCatalog.exceptions.BookAlreadyExistException;
import com.library.ServiceCatalog.exceptions.BookNotFoundException;
import com.library.ServiceCatalog.exceptions.BooksNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final BookForKafkaRepository bookForKafkaRepository;
    private final ImageService imageService;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String POPULAR_BOOKS_KEY = "popular:books:page:1";
    private static final int PAGE_SIZE = 20;
    private final Object synchronizationForMostPopularBooks = new Object();

    @Transactional
    public BookDTOForResponseCreate save(BookDTOForCreate bookDTO, MultipartFile coverImage) {
        Book book = toEntity(bookDTO);
        book.setBookAddedAt(LocalDateTime.now());
        log.info("Converted dto to entity. Title: '{}'", book.getBookName());
        bookRepository.save(book);
        if (coverImage != null && !coverImage.isEmpty()) {
            log.info("Processing cover image for book Title: {}", book.getBookName());
            String imageUrl = imageService.storeImage(coverImage, book.getBookId());
            book.setBookImage(imageUrl);
        }
        bookRepository.save(book);
        log.info("The book saved. ID: '{}'", book.getBookId());
        return toBookDTOForResponse(book);
    }

    @Transactional
    public void saveExpectedBookToCurrentBook(BookDTOForKafka book) {
        BookForKafka entity = toEntityForKafka(book);
        entity.setBookAddedAt(LocalDateTime.now());
        bookForKafkaRepository.save(entity);
        if (bookRepository.existsBookByBookId(entity.getBookId())){
            log.warn("The expected book is not saved to the current books. The same book already exists. ID: {}", entity.getBookId());
            throw new BookAlreadyExistException("Book already exist");
        }
        bookForKafkaRepository.save(entity);
        log.info("The expected book saved to the current books. ID: '{}'", entity.getBookId());

    }

    @Transactional
    public void delete(UUID bookId) {
        log.info("The book is deleted. ID: '{}'", bookId);
        bookRepository.deleteByBookId(bookId);
    }

    @Transactional(readOnly = true)
    public BookDtoWithTotalElements findAll(Pageable pageable) {
        Page<Book> booksPage = bookRepository.findAll(pageable);
        long bookCount = booksPage.getTotalElements();
        int bookPages = booksPage.getTotalPages();
        List<Book> books = booksPage.getContent();
        if (books.isEmpty()) {
            log.warn("Books not found");
            throw new BooksNotFoundException("Books not found");
        }
        List<BookDTOForResponseGetBook> bookDTOForResponseGetBooks = books.stream().map(this::toBookDTOForResponseGetBook).toList();
        BookDtoWithTotalElements bookDtoWithTotalElements = new BookDtoWithTotalElements();
        bookDtoWithTotalElements.setBookDTOForResponseGetBookList(bookDTOForResponseGetBooks);
        bookDtoWithTotalElements.setBookCount(bookCount);
        bookDtoWithTotalElements.setBookPages(bookPages);
        return bookDtoWithTotalElements;
    }
    @Transactional(readOnly = true)
    public BookDtoWithTotalElements findAllByGenre(String genre, Pageable pageable) {
        Page<Book> booksPage = bookRepository.findAllByBookGenre(genre, pageable);
        long bookCount = booksPage.getTotalElements();
        int bookPages = booksPage.getTotalPages();
        List<Book> books = booksPage.getContent();
        if (books.isEmpty()) {
            log.warn("Books not found");
            throw new BooksNotFoundException("Books not found");
        }
        List<BookDTOForResponseGetBook> bookDTOForResponseGetBooks = books.stream().map(this::toBookDTOForResponseGetBook).toList();
        BookDtoWithTotalElements bookDtoWithTotalElements = new BookDtoWithTotalElements();
        bookDtoWithTotalElements.setBookDTOForResponseGetBookList(bookDTOForResponseGetBooks);
        bookDtoWithTotalElements.setBookCount(bookCount);
        bookDtoWithTotalElements.setBookPages(bookPages);
        return bookDtoWithTotalElements;
    }

    public BookDTOForResponseGetBook findById(UUID id) {
        Optional<Book> book  = bookRepository.findByBookId(id);
        if (book.isEmpty()){
            log.warn("The book not found. ID: '{}'", id);
            throw new BookNotFoundException("The book not found");
        }
        log.info("The book were found. ID: '{}'", book.get().getBookId());
        return toBookDTOForResponseGetBook(book.get());
    }

    @Transactional
    public BookDTOForResponseCreate updateBook(BookDTO bookDTO, UUID id, MultipartFile coverImage) {
        log.info("Trying to find a book for updates. ID '{}'", id);
        Optional<Book> optionalBook = bookRepository.findByBookId(id);
        if (optionalBook.isEmpty()){
            log.warn("The book for update not found. ID: '{}'", id);
            throw new BookNotFoundException("The book not found");
        }
        Book book = optionalBook.get();
        log.info("Starting of updating the book. ID '{}'", id);
        if (bookDTO.getBookName() != null)
            book.setBookName(bookDTO.getBookName());
        if (bookDTO.getBookAuthor() != null)
            book.setBookAuthor(bookDTO.getBookAuthor());
        if (bookDTO.getBookYear() != 0)
            book.setBookYear(bookDTO.getBookYear());
        if (bookDTO.getBookPublication() != null)
            book.setBookPublication(bookDTO.getBookPublication());
        if (bookDTO.getBookLanguage() != null)
            book.setBookLanguage(bookDTO.getBookLanguage());
        if (bookDTO.getBookItems() != 0)
            book.setBookItems(bookDTO.getBookItems());
        if (coverImage != null && !coverImage.isEmpty()) {
            String imageUrl = imageService.storeImage(coverImage, book.getBookId());
            book.setBookImage(imageUrl);
        }
        if (bookDTO.getBookGenre() != null)
            book.setBookGenre(bookDTO.getBookGenre());
        bookRepository.save(book);
        log.info("The updated book was saved. ID '{}'", id);
        return toBookDTOForResponse(book);
    }

    @Transactional(readOnly = true)
    public List<BookDTOForResponseGetBook> findAllRecentlyAddedAt(int page, int booksPerPage) {
        Pageable pageable = PageRequest.of(page, booksPerPage, Sort.by(Sort.Direction.DESC, "bookAddedAt"));
        log.info("Trying to find all recently added books.");
        List<Book> books = bookRepository.findAllByOrderByBookAddedAtDesc(pageable);
        if (books.isEmpty()){
            log.warn("The recently added books were not found");
            throw new BooksNotFoundException("Books not found");
        }
        log.info("All recently added books were found");
        return books.stream().map(this::toBookDTOForResponseGetBook).toList();
    }
    @Transactional(readOnly = true)
    public List<CategoriesDTO> findCategories(){
        List<CategoriesDTO> categories = bookRepository.findAllCategories();
        if (categories.isEmpty()){
            log.warn("The categories not found.");
            throw new CategoriesNotFoundException("The categories not found");
        }
        return categories;
    }

    @Transactional(readOnly = true)
    public List<BookDTO> getMostPopularBooks(int page) {
        if (page < 0) {
            log.warn("Negative page. Page: {}", page);
            throw new IllegalArgumentException("Page index must not be negative");
        }
        if (page != 0) {
            log.info("Trying to find all most popular books");
            List<BookDTO> books = bookRepository.findMostPopularBooks(PageRequest.of(page, PAGE_SIZE)).stream().map(this::toDTO).toList();
            if (books.isEmpty()){
                log.warn("The most popular books were not found.");
                throw new BooksNotFoundException("Books not found");
            }
            log.info("All most popular books were found");
            return books;
        }
        log.info("Trying to get all most popular books from redis");
        List<BookDTO> redisBooks = getCacheFromRedis(POPULAR_BOOKS_KEY);
        if (redisBooks != null && !redisBooks.isEmpty()) {
            log.info("All most popular books were found in redis");
            return redisBooks;
        }
        return getBooksAndSaveToRedis(page);
    }

    private List<BookDTO> getBooksAndSaveToRedis(int page) {
        synchronized (synchronizationForMostPopularBooks) {
            List<BookDTO> redisBooksSync = getCacheFromRedis(POPULAR_BOOKS_KEY);
            if (redisBooksSync != null && !redisBooksSync.isEmpty()){
                log.info("All most popular books were found in redis");
                return redisBooksSync;
            }
            log.info("Trying to find all most popular books");
            List<BookDTO> books = bookRepository.findMostPopularBooks(PageRequest.of(page, PAGE_SIZE)).stream().map(this::toDTO).toList();
            if (books.isEmpty()){
                log.warn("The most popular books were not found");
                throw new BooksNotFoundException("Books not found");
            }
            log.info("Trying to save all most popular books in redis");
            redisTemplate.opsForValue().set(
                    POPULAR_BOOKS_KEY,
                    books,
                    Duration.ofHours(2)
            );
            log.info("All most popular books saved in redis");
            return books;

        }
    }
    private List<BookDTO> getCacheFromRedis(String key){
        try {
            return (List<BookDTO>) redisTemplate.opsForValue().get(key);
        }catch (Exception e){
            log.warn("Failed to get popular books from redis. Error: {}", e.getMessage());
            throw new RedisConnectionFailureException(e.getMessage());
        }
    }


    private BookDTO toDTO(Book book) {
        log.info("Mapping book entity to bookDTO for response. ID: '{}'", book.getBookId());
        return modelMapper.map(book, BookDTO.class);
    }

    private Book toEntity(BookDTOForCreate bookDTO) {
        log.info("Mapping bookDTO to entity. Title: '{}'", bookDTO.getBookName());
        Book book = new Book();
        book.setBookName(bookDTO.getBookName());
        book.setBookItems(bookDTO.getBookItems());
        book.setBookLanguage(bookDTO.getBookLanguage());
        book.setBookYear(bookDTO.getBookYear());
        book.setBookPublication(bookDTO.getBookPublication());
        book.setBookGenre(bookDTO.getBookGenre());
        book.setBookAuthor(bookDTO.getBookAuthor());
        return book;
    }

    private BookForKafka toEntityForKafka(BookDTOForKafka bookDTOForKafka) {
        if (bookDTOForKafka == null) {
            log.error("Attempted to convert null BookDTOForKafka to entity");
            throw new IllegalArgumentException("BookDTOForKafka cannot be null");
        }
        log.info("Mapping bookDTO to entity for kafka. ID: '{}'", bookDTOForKafka.getBookId());
        return modelMapper.map(bookDTOForKafka, BookForKafka.class);
    }

    private BookDTOForResponseCreate toBookDTOForResponse(Book book) {
        log.info("Mapping book entity to bookDTO for create response. ID: '{}'", book.getBookId());
        return modelMapper.map(book, BookDTOForResponseCreate.class);
    }

    private BookDTOForResponseGetBook toBookDTOForResponseGetBook(Book book) {
        log.info("Mapping book entity to bookDTO for get response. ID: '{}'", book.getBookId());
        return modelMapper.map(book, BookDTOForResponseGetBook.class);
    }


}
