package com.library.FrontendMicroservice.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
public class Book extends BaseBook{
    private UUID bookId;

}
