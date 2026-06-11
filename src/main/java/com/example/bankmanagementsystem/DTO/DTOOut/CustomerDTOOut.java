package com.example.bankmanagementsystem.DTO.DTOOut;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTOOut {
    private Integer id;
    private String username;
    private String name;
    private String email;
    private String phoneNumber;
}
