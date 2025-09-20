package ua.zakharchuk.ExpectedBooksService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.zakharchuk.ExpectedBooksService.models.ExpectedBook;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpectedBookRepositories extends JpaRepository<ExpectedBook, Integer> {

    void deleteByExpectedBookId(UUID expectedBookId);

    Optional<ExpectedBook> findByExpectedBookId(UUID expectedBookId);
}
