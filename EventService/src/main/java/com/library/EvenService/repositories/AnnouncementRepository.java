package com.library.EvenService.repositories;

import com.library.EvenService.dto.AnnouncementDTO;
import com.library.EvenService.dto.EventDTO;
import com.library.EvenService.models.Announcement;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, UUID> {

    @Query("SELECT a FROM Announcement a WHERE " +
            "(a.name) LIKE :search OR " +
            "(a.body) LIKE :search OR " +
            "(a.type) LIKE :search")
    Page<Announcement> findAllByNameOrBodyOrType(String search, Pageable pageable);
}
