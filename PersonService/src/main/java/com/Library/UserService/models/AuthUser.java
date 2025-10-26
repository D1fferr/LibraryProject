package com.Library.UserService.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Data
@Table(name = "auth_user")
public class AuthUser {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "id")
    @NotNull
    private UUID id;
    @NotNull(message = "Field username must not be empty")
    @Column(name = "user_name")
    private String username;
    @NotNull(message = "Field password must not be empty")
    @Column(name = "user_password")
    private String password;
    @Column(name = "user_role")
    private String role;

}
