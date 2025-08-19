package com.Library.UserService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UserDTO {
    @Size(min = 1,message = "Username must not be empty")
    private String username;
    @Email(message = "Email should be in format email@email.email")
    @Size(min = 1, message = "Email must not be empty")
    private String userEmail;
    private String userLibraryCode;
    @Size(min = 1, message = "Password must not be empty")
    private String userPassword;

    public UserDTO(String username, String userEmail, String userLibraryCode, String userPassword) {
        this.username = username;
        this.userEmail = userEmail;
        this.userLibraryCode = userLibraryCode;
        this.userPassword = userPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserLibraryCode() {
        return userLibraryCode;
    }

    public void setUserLibraryCode(String userLibraryCode) {
        this.userLibraryCode = userLibraryCode;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
