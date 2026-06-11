package com.example.bankmanagementsystem.DTO.DTOIn;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDTOIn {
    @NotNull(message = "from account id can not be null")
    private Integer fromAccountId;
    @NotNull(message = "to account id can not be null")
    private Integer toAccountId;
    @NotNull(message = "amount can not be null")
    @Positive(message = "amount must be positive")
    private Double amount;
}
