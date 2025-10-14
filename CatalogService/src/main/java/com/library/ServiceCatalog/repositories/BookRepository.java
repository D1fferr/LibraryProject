package com.library.ServiceCatalog.repositories;

import com.library.ServiceCatalog.dto.BookDTO;
import com.library.ServiceCatalog.models.Book;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
