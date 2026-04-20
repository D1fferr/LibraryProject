package com.library.FrontendMicroservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class UserPageDto {
    private List<UserDtoWithId> users;
    private int totalPages;
    private long totalElements;
}
