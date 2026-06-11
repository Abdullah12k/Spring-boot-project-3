package com.example.bankmanagementsystem.DTO.DTOOut;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTOOut {
    private Integer id;
    private String accountNumber;
    private Double balance;
    private Boolean isActive;
    private Integer customerId;
}
