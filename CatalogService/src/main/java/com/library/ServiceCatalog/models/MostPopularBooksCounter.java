package com.library.ServiceCatalog.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Table(name = "most_popular_books_counter")
public class MostPopularBooksCounter {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "id")
    private UUID id;

    @Column(name = "book_id")
    private UUID bookId;
    @Column(name = "crated_at")
    private LocalDateTime createdAt;
    @Column(name = "counter")
    private int counter;
}
