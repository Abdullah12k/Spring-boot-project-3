package com.example.bankmanagementsystem.DTO.DTOIn;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTOIn {
    @NotEmpty(message = "account number can not be empty")
    @Pattern(regexp = "^[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}$",
            message = "account number must follow format XXXX-XXXX-XXXX-XXXX")
    private String accountNumber;
    @NotNull(message = "balance can not be null")
    @PositiveOrZero(message = "balance can not be negative")
    private Double balance;
}
