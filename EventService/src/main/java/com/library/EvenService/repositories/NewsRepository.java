package com.library.EvenService.repositories;

import com.library.EvenService.models.News;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NewsRepository extends JpaRepository<News, UUID> {



    @Query("SELECT n FROM News n WHERE " +
            "(n.name) LIKE :search OR " +
            "(n.body) LIKE :search")
    Page<News> findAllByParam(String search, Pageable pageable);
}
