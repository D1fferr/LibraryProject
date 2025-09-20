package com.Library.UserService.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.UuidGenerator;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "person")
public class User {

    @Id
    @Column(name = "user_id")
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID userId;
    @Column(name = "user_name")
    @NotEmpty(message = "Username must not be empty")
    private String username;
    @Column(name = "user_email")
    @Email(message = "Email should be in format email@email.email")
    @NotEmpty(message = "Email must not be empty")
    private String userEmail;
    @Column(name = "user_library_code")
    private String userLibraryCode;
    @Column(name = "user_password")
    @NotEmpty(message = "Password must not be empty")
    private String userPassword;
    @Column(name = "user_role")
    private String userRole;

    public User(String username, String userEmail,
                String userLibraryCode, String userPassword,
                String userRole) {
        this.username = username;
        this.userEmail = userEmail;
        this.userLibraryCode = userLibraryCode;
        this.userPassword = userPassword;
        this.userRole = userRole;
    }

    public User() {}

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID personId) {
        this.userId = personId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String personUsername) {
        this.username = personUsername;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String personEmail) {
        this.userEmail = personEmail;
    }

    public String getUserLibraryCode() {
        return userLibraryCode;
    }

    public void setUserLibraryCode(String personLibraryId) {
        this.userLibraryCode = personLibraryId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String personPassword) {
        this.userPassword = personPassword;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String personRole) {
        this.userRole = personRole;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId && userLibraryCode == user.userLibraryCode && Objects.equals(username, user.username) && Objects.equals(userEmail, user.userEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, userEmail, userLibraryCode);
    }

    @Override
    public String toString() {
        return "Person{" +
                "personId=" + userId +
                ", personUsername='" + username + '\'' +
                ", personEmail='" + userEmail + '\'' +
                ", personLibraryId=" + userLibraryCode +
                ", personPassword='" + userPassword + '\'' +
                ", personRole='" + userRole + '\'' +
                '}';
    }
}
