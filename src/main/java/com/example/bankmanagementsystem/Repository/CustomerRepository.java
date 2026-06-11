package com.example.bankmanagementsystem.Repository;

import com.example.bankmanagementsystem.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findCustomerById(Integer id);
    Customer findCustomerByUserId(Integer userId);
    boolean existsByPhoneNumber(String phoneNumber);
}
