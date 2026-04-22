package ua.zakharchuk.ExpectedBooksService.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.zakharchuk.ExpectedBooksService.models.ExpectedBook;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpectedBookRepository extends JpaRepository<ExpectedBook, UUID> {

    void deleteByExpectedBookId(UUID expectedBookId);

    Optional<ExpectedBook> findByExpectedBookId(UUID expectedBookId);

    @Query("SELECT b FROM ExpectedBook b WHERE " +
            "b.expectedBookAuthor LIKE :search OR " +
            "b.expectedBookGenre LIKE :search OR " +
            "b.expectedBookPublication LIKE :search OR " +
            "b.expectedBookName LIKE :search")
    Page<ExpectedBook> findBooks(String search, Pageable pageable);

}
