package com.library.ServiceCatalog.repositories;

import com.library.ServiceCatalog.dto.BookDTO;
import com.library.ServiceCatalog.dto.BookDTOForResponseGetBook;
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
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    Optional<Book> findByBookId(UUID bookId);

    Page<Book> findAllByOrderByBookAddedAtDesc(Pageable pageable);

    void deleteByBookId(UUID bookId);

    boolean existsBookByBookId(UUID bookId);
    @Query("SELECT b FROM MostPopularBooksCounter c JOIN Book b ON c.bookId = b.bookId ORDER BY c.counter DESC")
    Page<Book> findMostPopularBooks(Pageable pageable);
    Page<Book> findAllByBookGenre(String genre, Pageable pageable);
    Page<Book> findAllByBookAuthor(String author, Pageable pageable);
    @Query("SELECT new com.library.ServiceCatalog.dto.CategoriesDTO(b.bookGenre, COUNT(b)) " +
            "from Book b GROUP BY b.bookGenre order by COUNT(b) DESC")
    List<CategoriesDTO> findAllCategories();

    List<Book> findAllByBookIdIn(Collection<UUID> bookIds);

    @Query("SELECT b FROM Book b WHERE " +
            "b.bookAuthor LIKE :search OR " +
            "b.bookGenre LIKE :search OR " +
            "b.bookPublication LIKE :search OR " +
            "b.bookName LIKE :search")
    Page<Book> findBooks(String search, Pageable pageable);
}
