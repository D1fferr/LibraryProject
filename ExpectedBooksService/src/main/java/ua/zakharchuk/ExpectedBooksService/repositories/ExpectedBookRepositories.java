package ua.zakharchuk.ExpectedBooksService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.zakharchuk.ExpectedBooksService.models.ExpectedBook;
@Repository
public interface ExpectedBookRepositories extends JpaRepository<ExpectedBook, Integer> {

}
