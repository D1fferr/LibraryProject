package com.library.ServiceCatalog.repositories;

import com.library.ServiceCatalog.models.MostPopularBooksCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MostPopularBooksCounterRepository extends JpaRepository<MostPopularBooksCounter, UUID> {


    Optional<MostPopularBooksCounter> findByBookId(UUID bookId);
}
