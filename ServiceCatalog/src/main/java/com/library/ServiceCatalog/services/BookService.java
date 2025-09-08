package com.library.ServiceCatalog.services;

import com.library.ServiceCatalog.dto.BookDTO;
import com.library.ServiceCatalog.models.Book;
import com.library.ServiceCatalog.repositories.BookRepository;
import com.library.ServiceCatalog.util.BookNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;

    public BookService(BookRepository bookRepository, ModelMapper modelMapper, MessageSource messageSource) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
    }

    @Transactional
    public void save(BookDTO bookDTO) {
        Book book = toEntity(bookDTO);
        book.setBookAddedAt(LocalDateTime.now());
        bookRepository.save(book);
    }

    @Transactional
    public void delete(UUID bookId) {
        bookRepository.deleteByBookId(bookId);
    }

    public List<BookDTO> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream().map(this::toDTO).toList();
    }

    public BookDTO findById(UUID id) {
        if (bookRepository.findByBookId(id).isPresent())
            return toDTO(bookRepository.findByBookId(id).get());
        else
            throw new BookNotFoundException(messageSource
                    .getMessage("book.not.found.message", new Object[0], Locale.ENGLISH));
    }
    @Transactional
    public void updateBook(BookDTO bookDTO, UUID id) {
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
        if (bookDTO.getBookImage() != null)
            book.setBookImage(bookDTO.getBookImage());
        if (bookDTO.getBookGenre() != null)
            book.setBookGenre(bookDTO.getBookGenre());
        bookRepository.save(book);

    }
    public Optional<Book> findByNameAndAuthor(String bookName, String bookAuthor) {
        return bookRepository.findByBookNameAndBookAuthor(bookName, bookAuthor);
    }

    public List<BookDTO> findAllRecentlyAddedAt(int page, int booksPerPage){
        Pageable pageable = PageRequest.of(page, booksPerPage, Sort.by("bookAddedAt"));
        return bookRepository.findAllByOrderByBookAddedAtDesc(pageable)
                .stream().map(this::toDTO).toList();
    }


    private BookDTO toDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    private Book toEntity(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, Book.class);
    }


}
