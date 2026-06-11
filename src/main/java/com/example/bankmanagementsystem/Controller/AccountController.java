package com.example.bankmanagementsystem.Controller;

import com.example.bankmanagementsystem.Api.ApiResponse;
import com.example.bankmanagementsystem.DTO.DTOIn.AccountDTOIn;
import com.example.bankmanagementsystem.DTO.DTOIn.TransferDTOIn;
import com.example.bankmanagementsystem.Model.User;
import com.example.bankmanagementsystem.Service.AccountService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create/{customerId}")
    public ResponseEntity<?> createAccount(@PathVariable Integer customerId, @RequestBody @Valid AccountDTOIn dto) {
        accountService.createAccount(customerId, dto);
        return ResponseEntity.ok(new ApiResponse("Account created successfully"));
    }

    @PutMapping("/activate/{accountId}")
    public ResponseEntity<?> activateAccount(@PathVariable Integer accountId) {
        accountService.activateAccount(accountId);
        return ResponseEntity.ok(new ApiResponse("Account activated successfully"));
    }

    @PutMapping("/block/{accountId}")
    public ResponseEntity<?> blockAccount(@PathVariable Integer accountId) {
        accountService.blockAccount(accountId);
        return ResponseEntity.ok(new ApiResponse("Account blocked successfully"));
    }

    @PutMapping("/update/{accountId}")
    public ResponseEntity<?> updateAccount(@PathVariable Integer accountId,
            @RequestBody @Valid AccountDTOIn dto) {
        accountService.updateAccount(accountId, dto);
        return ResponseEntity.ok(new ApiResponse("Account updated successfully"));
    }

    @DeleteMapping("/delete/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable Integer accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok(new ApiResponse("Account deleted successfully"));
    }

    @GetMapping("/details/{accountId}")
    public ResponseEntity<?> getAccountById(@PathVariable Integer accountId) {
        return ResponseEntity.ok(accountService.getAccountById(accountId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getCustomerAccounts(@PathVariable Integer customerId) {
        return ResponseEntity.ok(accountService.getCustomerAccounts(customerId));
    }

    @GetMapping("/my-accounts")
    public ResponseEntity<?> getMyAccounts(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getMyAccounts(user.getId()));
    }

    @PostMapping("/deposit/{accountId}")
    public ResponseEntity<?> deposit(
            @AuthenticationPrincipal User user,
            @PathVariable Integer accountId,
            @RequestParam Double amount) {
        accountService.deposit(user.getId(), accountId, amount);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse("Money deposited successfully"));
    }

    @PostMapping("/withdraw/{accountId}")
    public ResponseEntity<?> withdraw(@AuthenticationPrincipal User user, @PathVariable Integer accountId,
            @RequestParam Double amount) {
        accountService.withdraw(user.getId(), accountId, amount);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Money withdrawn successfully"));
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@AuthenticationPrincipal User user, @RequestBody @Valid TransferDTOIn dto) {
        accountService.transfer(user.getId(), dto);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Money transferred successfully"));
    }
}
