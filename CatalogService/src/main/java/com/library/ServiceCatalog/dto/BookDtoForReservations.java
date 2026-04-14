package com.library.ServiceCatalog.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class BookDtoForReservations {
    private List<UUID> uuids;
}
