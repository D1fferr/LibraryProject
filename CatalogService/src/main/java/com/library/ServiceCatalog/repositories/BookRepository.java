package com.library.ServiceCatalog.repositories;

import com.library.ServiceCatalog.dto.BookDTO;
import com.library.ServiceCatalog.dto.CategoriesDTO;
import com.library.ServiceCatalog.models.Book;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.net.ContentHandler;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findByBookId(UUID bookId);
    Optional<Book> findByBookNameAndBookAuthor(String bookName, String bookAuthor);

    List<Book> findAllByOrderByBookAddedAtDesc(Pageable pageable);

    void deleteByBookId(UUID bookId);

    boolean existsBookByBookId(UUID bookId);
    @Query("SELECT b FROM MostPopularBooksCounter c JOIN Book b ON c.bookId = b.bookId ORDER BY c.counter DESC")
    List<Book> findMostPopularBooks(Pageable pageable);
    Page<Book> findAllByBookGenre(@NotEmpty(message = "Book genre cannot be empty.") String bookGenre, Pageable pageable);
    @Query("SELECT new com.library.ServiceCatalog.dto.CategoriesDTO(b.bookGenre, COUNT(b)) " +
            "from Book b GROUP BY b.bookGenre order by COUNT(b) DESC")
    List<CategoriesDTO> findAllCategories();
}
