package ua.zakharchuk.ExpectedBooksService;

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
import ua.zakharchuk.ExpectedBooksService.dtos.ExpectedBookDTO;
import ua.zakharchuk.ExpectedBooksService.dtos.ExpectedBookDTOCreate;
import ua.zakharchuk.ExpectedBooksService.exceptions.ExpectedBookNotFoundException;
import ua.zakharchuk.ExpectedBooksService.exceptions.ExpectedBooksNotFoundException;
import ua.zakharchuk.ExpectedBooksService.models.ExpectedBook;
import ua.zakharchuk.ExpectedBooksService.repositories.ExpectedBookRepository;
import ua.zakharchuk.ExpectedBooksService.services.ExpectedBookService;
import ua.zakharchuk.ExpectedBooksService.services.ImageService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpectedBookServiceTest {

    @Mock
    private ExpectedBookRepository bookRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private MultipartFile coverImage;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ExpectedBookService bookService;

    @Test
    void save_BookWithoutImage_ShouldSave() {

        ExpectedBookDTOCreate bookDTO = new ExpectedBookDTOCreate();
        bookDTO.setExpectedBookAuthor("Test");
        bookDTO.setExpectedBookLanguage("Test");
        bookDTO.setExpectedBookGenre("Test");
        bookDTO.setExpectedBookName("Test");
        bookDTO.setExpectedBookPieces(1);
        bookDTO.setExpectedBookYear(1999);
        bookDTO.setExpectedBookPublication("Test");
        ExpectedBook book = new ExpectedBook();
        when(modelMapper.map(bookDTO, ExpectedBook.class)).thenReturn(book);
        when(bookRepository.save(any(ExpectedBook.class))).thenReturn(book);

        bookService.save(bookDTO, null);

        verify(bookRepository, atLeastOnce()).save(any(ExpectedBook.class));
        verify(imageService, never()).storeImage(any(), any());
    }
    @Test
    void save_BookWithImage_ShouldSaveWithImage() {
        ExpectedBookDTOCreate bookDTO = new ExpectedBookDTOCreate();
        bookDTO.setExpectedBookAuthor("Test");
        bookDTO.setExpectedBookLanguage("Test");
        bookDTO.setExpectedBookGenre("Test");
        bookDTO.setExpectedBookName("Test");
        bookDTO.setExpectedBookPieces(1);
        bookDTO.setExpectedBookYear(1999);
        bookDTO.setExpectedBookPublication("Test");
        when(modelMapper.map(bookDTO, ExpectedBook.class)).thenReturn(new ExpectedBook());
        when(coverImage.isEmpty()).thenReturn(false);
        when(bookRepository.save(any())).thenReturn(new ExpectedBook());
        when(imageService.storeImage(any(), any())).thenReturn("url");

        bookService.save(bookDTO, coverImage);
        verify(imageService).storeImage(any(), any());
    }
    @Test
    void save_EmptyImage_ShouldNotProcessImage() {
        ExpectedBookDTOCreate bookDTO = new ExpectedBookDTOCreate();
        bookDTO.setExpectedBookAuthor("Test");
        bookDTO.setExpectedBookLanguage("Test");
        bookDTO.setExpectedBookGenre("Test");
        bookDTO.setExpectedBookName("Test");
        bookDTO.setExpectedBookPieces(1);
        bookDTO.setExpectedBookYear(1999);
        bookDTO.setExpectedBookPublication("Test");

        ExpectedBook book = new ExpectedBook();

        when(modelMapper.map(bookDTO, ExpectedBook.class)).thenReturn(book);

        when(coverImage.isEmpty()).thenReturn(true);
        when(bookRepository.save(any(ExpectedBook.class))).thenReturn(book);

        bookService.save(bookDTO, coverImage);

        verify(imageService, never()).storeImage(any(), any());
        verify(bookRepository, atLeastOnce()).save(any(ExpectedBook.class));
    }
    @Test
    void findAll_BooksExist_ShouldReturnList() {
        Pageable pageable = mock(Pageable.class);
        List<ExpectedBook> books = List.of(new ExpectedBook(), new ExpectedBook());
        Page<ExpectedBook> page = new PageImpl<>(books);

        when(bookRepository.findAll(pageable)).thenReturn(page);

        List<ExpectedBookDTO> result = bookService.findAll(pageable).getExpectedBooks();

        verify(bookRepository).findAll(pageable);
        assertNotNull(result);
    }
    @Test
    void findAll_NoBooks_ShouldThrowException() {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        Page<ExpectedBook> emptyPage = new PageImpl<>(Collections.emptyList());

        when(bookRepository.findAll(pageable)).thenReturn(emptyPage);

        // Act & Assert
        assertThrows(ExpectedBooksNotFoundException.class, () -> {
            bookService.findAll(pageable);
        });

        verify(bookRepository).findAll(pageable);
    }
    @Test
    void updateBook_BookExists_ShouldUpdate() {
        // Arrange
        UUID bookId = UUID.randomUUID();
        ExpectedBookDTOCreate bookDTO = new ExpectedBookDTOCreate();
        bookDTO.setExpectedBookAuthor("Test");
        bookDTO.setExpectedBookLanguage("Test");
        bookDTO.setExpectedBookGenre("Test");
        bookDTO.setExpectedBookName("Test");
        bookDTO.setExpectedBookPieces(1);
        bookDTO.setExpectedBookYear(1999);
        bookDTO.setExpectedBookPublication("Test");

        ExpectedBook existingBook = new ExpectedBook();
        existingBook.setExpectedBookId(bookId);
        when(bookRepository.findByExpectedBookId(bookId)).thenReturn(Optional.of(existingBook));

        // Act
        ExpectedBookDTO result = bookService.update(bookId, bookDTO, null);

        // Assert
        verify(bookRepository).save(existingBook);

    }
    @Test
    void updateBook_BookNotFound_ShouldThrowException() {
        // Arrange
        UUID bookId = UUID.randomUUID();
        ExpectedBookDTOCreate bookDTO = new ExpectedBookDTOCreate();
        bookDTO.setExpectedBookAuthor("Test");
        bookDTO.setExpectedBookLanguage("Test");
        bookDTO.setExpectedBookGenre("Test");
        bookDTO.setExpectedBookName("Test");
        bookDTO.setExpectedBookPieces(1);
        bookDTO.setExpectedBookYear(1999);
        bookDTO.setExpectedBookPublication("Test");


        when(bookRepository.findByExpectedBookId(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ExpectedBookNotFoundException.class, () -> {
            bookService.update(bookId, bookDTO, null);
        });
    }
    @Test
    void updateBook_WithImage_ShouldUpdateImage() {
        // Arrange
        UUID bookId = UUID.randomUUID();
        ExpectedBookDTOCreate bookDTO = new ExpectedBookDTOCreate();
        bookDTO.setExpectedBookAuthor("Test");
        bookDTO.setExpectedBookLanguage("Test");
        bookDTO.setExpectedBookGenre("Test");
        bookDTO.setExpectedBookName("Test");
        bookDTO.setExpectedBookPieces(1);
        bookDTO.setExpectedBookYear(1999);
        bookDTO.setExpectedBookPublication("Test");
        ExpectedBook existingBook = new ExpectedBook();
        existingBook.setExpectedBookId(bookId);

        when(bookRepository.findByExpectedBookId(bookId)).thenReturn(Optional.of(existingBook));
        when(coverImage.isEmpty()).thenReturn(false);
        when(imageService.storeImage(any(), any())).thenReturn("new_image_url");

        // Act
        bookService.update(bookId, bookDTO, coverImage);

        // Assert
        verify(imageService).storeImage(coverImage, bookId);
    }
    @Test
    void findById_BookExists_ShouldReturnDTO() {
        // Arrange
        UUID id = UUID.randomUUID();
        ExpectedBook expectedBook = new ExpectedBook();
        when(bookRepository.findByExpectedBookId(id)).thenReturn(Optional.of(expectedBook));

        // Act
        bookRepository.findByExpectedBookId(id);

        // Assert
        verify(bookRepository).findByExpectedBookId(id);
    }

    @Test
    void findById_BookNotFound_ShouldThrowException() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(bookRepository.findByExpectedBookId(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ExpectedBookNotFoundException.class, () -> {
            bookService.findById(id);
        });
    }

    @Test
    void findById_VerifyRepositoryCalled() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(bookRepository.findByExpectedBookId(id)).thenReturn(Optional.of(new ExpectedBook()));

        // Act
        bookService.findById(id);

        // Assert
        verify(bookRepository).findByExpectedBookId(id);
    }

}
