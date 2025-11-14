package com.library.ServiceCatalog.services;

import com.library.ServiceCatalog.dto.BookDTO;
import com.library.ServiceCatalog.dto.BookDTOForKafka;
import com.library.ServiceCatalog.dto.BookDTOForResponseCreate;
import com.library.ServiceCatalog.dto.BookDTOForResponseGetBook;
import com.library.ServiceCatalog.models.Book;
import com.library.ServiceCatalog.models.BookForKafka;
import com.library.ServiceCatalog.repositories.BookForKafkaRepository;
import com.library.ServiceCatalog.repositories.BookRepository;
import com.library.ServiceCatalog.util.BookAlreadyExitException;
import com.library.ServiceCatalog.util.BookNotFoundException;
import com.library.ServiceCatalog.util.BooksNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;
    private final BookForKafkaRepository bookForKafkaRepository;
    private final ImageService imageService;

    @Transactional
    public BookDTOForResponseCreate save(BookDTO bookDTO , MultipartFile coverImage) {
        Book book = toEntity(bookDTO);

        book.setBookAddedAt(LocalDateTime.now());
        bookRepository.save(book);
        if (coverImage != null && !coverImage.isEmpty()) {
            String imageUrl = imageService.storeImage(coverImage, book.getBookId());
            book.setBookImage(imageUrl);
        }
        bookRepository.save(book);
        return toBookDTOForResponse(book);
    }
    @Transactional
    public void saveExpectedBookToCurrentBook(BookDTOForKafka book) {
        BookForKafka entity = toEntityForKafka(book);
        entity.setBookAddedAt(LocalDateTime.now());
        if (bookRepository.existsBookByBookId(entity.getBookId()))
            throw new BookAlreadyExitException("");
        bookForKafkaRepository.save(entity);

    }

    @Transactional
    public void delete(UUID bookId) {
        bookRepository.deleteByBookId(bookId);
    }
    @Transactional(readOnly = true)
    public List<BookDTOForResponseGetBook> findAll(Pageable pageable) {

        List<Book> books = bookRepository.findAll(pageable).getContent();
        if (books.isEmpty())
            throw new BooksNotFoundException("Books not found");
        return books.stream().map(this::toBookDTOForResponseGetBook).toList();
    }

    public BookDTOForResponseGetBook findById(UUID id) {
        return bookRepository.findByBookId(id)
                .map(this::toBookDTOForResponseGetBook)
                .orElseThrow(()->new BookNotFoundException("The book not found"));
    }
    @Transactional
    public BookDTOForResponseCreate updateBook(BookDTO bookDTO, UUID id, MultipartFile coverImage) {
        Book book = bookRepository.findByBookId(id)
                .orElseThrow(() -> new BookNotFoundException(messageSource
                        .getMessage("book.not.found.message", new Object[0], Locale.ENGLISH)));
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
        if (bookDTO.getBookPieces() != 0)
            book.setBookPieces(bookDTO.getBookPieces());
        if (coverImage != null && !coverImage.isEmpty()) {
            String imageUrl = imageService.storeImage(coverImage, book.getBookId());
            book.setBookImage(imageUrl);
        }
        if (bookDTO.getBookGenre() != null)
            book.setBookGenre(bookDTO.getBookGenre());
        bookRepository.save(book);
        return toBookDTOForResponse(book);
    }
    @Transactional(readOnly = true)
    public Optional<Book> findByNameAndAuthor(String bookName, String bookAuthor) {
        return bookRepository.findByBookNameAndBookAuthor(bookName, bookAuthor);
    }
    @Transactional(readOnly = true)
    public List<BookDTOForResponseGetBook> findAllRecentlyAddedAt(int page, int booksPerPage){
        Pageable pageable = PageRequest.of(page, booksPerPage, Sort.by("bookAddedAt"));
        List<Book> books = bookRepository.findAllByOrderByBookAddedAtDesc(pageable);
        if (books.isEmpty())
            throw new BooksNotFoundException("Books not found");
        return books.stream().map(this::toBookDTOForResponseGetBook).toList();
    }
    @Transactional(readOnly = true)
    public List<BookDTO> getMostPopularBooks(Pageable pageable){
        List<BookDTO> books = bookRepository.findMostPopularBooks(pageable).stream().map(this::toDTO).toList();
        if (books.isEmpty())
            throw new BooksNotFoundException("Books not found");
        return books;
    }


    private BookDTO toDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    private Book toEntity(BookDTO bookDTO) {
        Book book = new Book();
        book.setBookName(bookDTO.getBookName());
        book.setBookPieces(bookDTO.getBookPieces());
        book.setBookLanguage(bookDTO.getBookLanguage());
        book.setBookYear(bookDTO.getBookYear());
        book.setBookPublication(bookDTO.getBookPublication());
        book.setBookGenre(bookDTO.getBookGenre());
        book.setBookAuthor(bookDTO.getBookAuthor());
        return book;
    }
    private BookForKafka toEntityForKafka(BookDTOForKafka bookDTOForKafka) {
        return modelMapper.map(bookDTOForKafka, BookForKafka.class);
    }
    private BookDTOForResponseCreate toBookDTOForResponse(Book book){
        return modelMapper.map(book, BookDTOForResponseCreate.class);
    }
    private BookDTOForResponseGetBook toBookDTOForResponseGetBook(Book book){
        return modelMapper.map(book, BookDTOForResponseGetBook.class);
    }


}
