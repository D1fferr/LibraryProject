package com.library.ServiceCatalog.repositories;

import com.library.ServiceCatalog.models.BookForKafka;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface BookForKafkaRepository extends JpaRepository<BookForKafka, UUID> {
}
