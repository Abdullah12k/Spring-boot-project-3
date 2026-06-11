package com.example.bankmanagementsystem.DTO.DTOIn;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterEmployeeDTOIn {
    @NotEmpty(message = "username can not be empty")
    @Size(min = 4, max = 10, message = "username length must be between 4 and 10 characters")
    private String username;
    @NotEmpty(message = "password can not be empty")
    @Size(min = 6, message = "password can not be less than 6 characters")
    private String password;
    @NotEmpty(message = "name can not be empty")
    @Size(min = 2, max = 20, message = "name length must be between 2 and 20 characters")
    private String name;
    @NotEmpty(message = "email can not be empty")
    @Email(message = "email must be valid")
    private String email;
    @NotEmpty(message = "position can not be empty")
    private String position;
    @NotNull(message = "salary can not be null")
    @PositiveOrZero(message = "salary can not be negative")
    private Double salary;
}
