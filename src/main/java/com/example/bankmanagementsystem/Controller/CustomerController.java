package com.example.bankmanagementsystem.Controller;

import com.example.bankmanagementsystem.Api.ApiResponse;
import com.example.bankmanagementsystem.DTO.DTOIn.RegisterCustomerDTOIn;
import com.example.bankmanagementsystem.Model.User;
import com.example.bankmanagementsystem.Service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody @Valid RegisterCustomerDTOIn dto) {
        customerService.registerCustomer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Customer registered successfully"));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/get/{customerId}")
    public ResponseEntity<?> getCustomerById(@PathVariable Integer customerId) {
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }

    @GetMapping("/my-profile")
    public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getMyProfile(user.getId()));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateMyProfile(@AuthenticationPrincipal User user, @RequestBody @Valid RegisterCustomerDTOIn dto) {
        customerService.updateMyProfile(user.getId(), dto);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Customer updated successfully"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMyProfile(@AuthenticationPrincipal User user) {
        customerService.deleteMyProfile(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Customer deleted successfully"));
    }

    @GetMapping("/my-accounts")
    public ResponseEntity<?> getMyAccounts(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getMyAccounts(user.getId()));
    }

    @PutMapping("/update/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable Integer customerId, @RequestBody @Valid RegisterCustomerDTOIn dto) {
        customerService.updateCustomer(customerId, dto);
        return ResponseEntity.ok(new ApiResponse("Customer updated successfully"));
    }

    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Integer customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.ok(new ApiResponse("Customer deleted successfully"));
    }

    @GetMapping("/accounts/{customerId}")
    public ResponseEntity<?> getCustomerAccounts(@PathVariable Integer customerId) {
        return ResponseEntity.ok(customerService.getCustomerAccounts(customerId));
    }
}
