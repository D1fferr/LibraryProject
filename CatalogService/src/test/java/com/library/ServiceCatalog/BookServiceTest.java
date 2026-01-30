package com.library.ServiceCatalog;


import com.library.ServiceCatalog.dto.BookDTO;
import com.library.ServiceCatalog.dto.BookDTOForResponseCreate;
import com.library.ServiceCatalog.dto.BookDTOForResponseGetBook;
import com.library.ServiceCatalog.exceptions.BookNotFoundException;
import com.library.ServiceCatalog.exceptions.BooksNotFoundException;
import com.library.ServiceCatalog.models.Book;
import com.library.ServiceCatalog.repositories.BookRepository;
import com.library.ServiceCatalog.services.BookService;
import com.library.ServiceCatalog.services.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {


    @Mock
    private BookRepository bookRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private MultipartFile coverImage;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    void save_BookWithoutImage_ShouldSave() {
        UUID id = UUID.randomUUID();
        BookDTO bookDTO = new BookDTO();
        bookDTO.setBookAuthor("Test");
        bookDTO.setBookLanguage("Test");
        bookDTO.setBookGenre("Test");
        bookDTO.setBookName("Test");
        bookDTO.setBookPieces(1);
        bookDTO.setBookYear(1999);
        bookDTO.setBookPublication("Test");
        Book book = new Book();
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        bookService.save(bookDTO, null);

        verify(bookRepository, atLeastOnce()).save(any(Book.class));
        verify(imageService, never()).storeImage(any(), any());
    }

    @Test
    void save_BookWithImage_ShouldSaveWithImage() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setBookAuthor("Test");
        bookDTO.setBookLanguage("Test");
        bookDTO.setBookGenre("Test");
        bookDTO.setBookName("Test");
        bookDTO.setBookPieces(1);
        bookDTO.setBookYear(1999);
        bookDTO.setBookPublication("Test");

        when(coverImage.isEmpty()).thenReturn(false);
        when(bookRepository.save(any())).thenReturn(new Book());
        when(imageService.storeImage(any(), any())).thenReturn("url");

        bookService.save(bookDTO, coverImage);
        verify(imageService).storeImage(any(), any());
    }
    @Test
    void save_EmptyImage_ShouldNotProcessImage() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setBookAuthor("Test");
        bookDTO.setBookLanguage("Test");
        bookDTO.setBookGenre("Test");
        bookDTO.setBookName("Test");
        bookDTO.setBookPieces(1);
        bookDTO.setBookYear(1999);
        bookDTO.setBookPublication("Test");


        Book book = new Book();

        when(coverImage.isEmpty()).thenReturn(true);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        bookService.save(bookDTO, coverImage);

        verify(imageService, never()).storeImage(any(), any());
        verify(bookRepository, atLeastOnce()).save(any(Book.class));
    }

    @Test
    void findAll_BooksExist_ShouldReturnList() {
        Pageable pageable = mock(Pageable.class);
        List<Book> books = List.of(new Book(), new Book());
        Page<Book> page = new PageImpl<>(books);

        when(bookRepository.findAll(pageable)).thenReturn(page);

        List<BookDTOForResponseGetBook> result = bookService.findAll(pageable);

        verify(bookRepository).findAll(pageable);
        assertNotNull(result);
    }
    @Test
    void findAll_NoBooks_ShouldThrowException() {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        Page<Book> emptyPage = new PageImpl<>(Collections.emptyList());

        when(bookRepository.findAll(pageable)).thenReturn(emptyPage);

        // Act & Assert
        assertThrows(BooksNotFoundException.class, () -> {
            bookService.findAll(pageable);
        });

        verify(bookRepository).findAll(pageable);
    }
    @Test
    void updateBook_BookExists_ShouldUpdate() {
        // Arrange
        UUID bookId = UUID.randomUUID();
        BookDTO bookDTO = new BookDTO();
        bookDTO.setBookAuthor("Test");
        bookDTO.setBookLanguage("Test");
        bookDTO.setBookGenre("Test");
        bookDTO.setBookName("Test");
        bookDTO.setBookPieces(1);
        bookDTO.setBookYear(1999);
        bookDTO.setBookPublication("Test");

        Book existingBook = new Book();
        existingBook.setBookId(bookId);
        when(bookRepository.findByBookId(bookId)).thenReturn(Optional.of(existingBook));

        // Act
        BookDTOForResponseCreate result = bookService.updateBook(bookDTO, bookId, null);

        // Assert
        verify(bookRepository).save(existingBook);

    }
    @Test
    void updateBook_BookNotFound_ShouldThrowException() {
        // Arrange
        UUID bookId = UUID.randomUUID();
        BookDTO bookDTO = new BookDTO();
        bookDTO.setBookAuthor("Test");
        bookDTO.setBookLanguage("Test");
        bookDTO.setBookGenre("Test");
        bookDTO.setBookName("Test");
        bookDTO.setBookPieces(1);
        bookDTO.setBookYear(1999);
        bookDTO.setBookPublication("Test");


        when(bookRepository.findByBookId(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BookNotFoundException.class, () -> {
            bookService.updateBook(bookDTO, bookId, null);
        });
    }
    @Test
    void updateBook_WithImage_ShouldUpdateImage() {
        // Arrange
        UUID bookId = UUID.randomUUID();
        BookDTO bookDTO = new BookDTO();
        Book existingBook = new Book();
        existingBook.setBookId(bookId);

        when(bookRepository.findByBookId(bookId)).thenReturn(Optional.of(existingBook));
        when(coverImage.isEmpty()).thenReturn(false);
        when(imageService.storeImage(any(), any())).thenReturn("new_image_url");

        // Act
        bookService.updateBook(bookDTO, bookId, coverImage);

        // Assert
        verify(imageService).storeImage(coverImage, bookId);
    }
    @Test
    void findAllRecentlyAddedAt_BooksExist_ShouldReturnList() {
        // Arrange
        List<Book> books = List.of(new Book(), new Book());
        when(bookRepository.findAllByOrderByBookAddedAtDesc(any(Pageable.class)))
                .thenReturn(books);

        // Act
        List<BookDTOForResponseGetBook> result = bookService.findAllRecentlyAddedAt(0, 10);

        // Assert
        verify(bookRepository).findAllByOrderByBookAddedAtDesc(any(Pageable.class));
        assertNotNull(result);
    }
    @Test
    void findAllRecentlyAddedAt_NoBooks_ShouldThrowException() {
        // Arrange
        when(bookRepository.findAllByOrderByBookAddedAtDesc(any(Pageable.class)))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(BooksNotFoundException.class, () -> {
            bookService.findAllRecentlyAddedAt(0, 10);
        });
    }

    @Test
    void findAllRecentlyAddedAt_WithDifferentPageAndSize() {
        // Arrange
        List<Book> books = List.of(new Book());
        when(bookRepository.findAllByOrderByBookAddedAtDesc(any(Pageable.class)))
                .thenReturn(books);

        // Act
        bookService.findAllRecentlyAddedAt(2, 5);

        // Assert
        verify(bookRepository).findAllByOrderByBookAddedAtDesc(any(Pageable.class));
    }
}

