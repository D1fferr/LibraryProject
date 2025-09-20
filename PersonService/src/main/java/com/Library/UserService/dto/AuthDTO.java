package com.Library.UserService.dto;


import jakarta.validation.constraints.Size;

public class AuthDTO {
    @Size(min = 1, message = "Username must not be empty")
    private String username;
    @Size(min = 1, message = "Password must not be empty")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
