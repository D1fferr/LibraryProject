package com.library.FrontendMicroservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AnnouncementDTOWithTotalElements {
    private List<AnnouncementDTOForGetRequest> announcements;
    private long announcementCount;
    private int announcementPages;
}
