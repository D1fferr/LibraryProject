package com.library.EvenService.dto;


import jakarta.validation.constraints.Size;

public class NewsDTO {

    @Size(min = 1, max = 10000, message = "News must be between 1 and 1000 characters long.")
    private String body;
    @Size(min = 1, max = 100, message = "News name must be between 1 and 100 characters long.")
    private String name;
    private String photo;

    public NewsDTO(){}


    public NewsDTO(String body, String name, String photo) {
        this.body = body;
        this.name = name;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
