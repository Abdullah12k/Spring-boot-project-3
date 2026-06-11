package com.example.bankmanagementsystem.Service;

import com.example.bankmanagementsystem.Api.ApiException;
import com.example.bankmanagementsystem.DTO.DTOIn.AccountDTOIn;
import com.example.bankmanagementsystem.DTO.DTOIn.TransferDTOIn;
import com.example.bankmanagementsystem.DTO.DTOOut.AccountDTOOut;
import com.example.bankmanagementsystem.Model.Account;
import com.example.bankmanagementsystem.Model.Customer;
import com.example.bankmanagementsystem.Repository.AccountRepository;
import com.example.bankmanagementsystem.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    private AccountDTOOut convertToDTO(Account account) {
        return new AccountDTOOut(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getIsActive(),
                account.getCustomer().getId()
        );
    }

    public void createAccount(Integer customerId, AccountDTOIn dto) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        if (accountRepository.existsByAccountNumber(dto.getAccountNumber())) {
            throw new ApiException("Account number already used");
        }

        Account account = new Account();
        account.setAccountNumber(dto.getAccountNumber());
        account.setBalance(dto.getBalance());
        account.setIsActive(false);
        account.setCustomer(customer);
        accountRepository.save(account);
    }

    public void activateAccount(Integer accountId) {
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new ApiException("Account not found");
        }
        account.setIsActive(true);
        accountRepository.save(account);
    }

    public void blockAccount(Integer accountId) {
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new ApiException("Account not found");
        }
        account.setIsActive(false);
        accountRepository.save(account);
    }

    public void updateAccount(Integer accountId, AccountDTOIn dto) {
        Account oldAccount = accountRepository.findAccountById(accountId);
        if (oldAccount == null) {
            throw new ApiException("Account not found");
        }

        Account accountWithNumber = accountRepository.findAccountByAccountNumber(dto.getAccountNumber());
        if (accountWithNumber != null && !accountWithNumber.getId().equals(accountId)) {
            throw new ApiException("Account number already used");
        }

        oldAccount.setAccountNumber(dto.getAccountNumber());
        oldAccount.setBalance(dto.getBalance());
        accountRepository.save(oldAccount);
    }

    public void deleteAccount(Integer accountId) {
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new ApiException("Account not found");
        }
        accountRepository.delete(account);
    }

    public AccountDTOOut getAccountById(Integer accountId) {
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new ApiException("Account not found");
        }
        return convertToDTO(account);
    }

    public List<AccountDTOOut> getCustomerAccounts(Integer customerId) {
        if (customerRepository.findCustomerById(customerId) == null) {
            throw new ApiException("Customer not found");
        }
        List<Account> accounts = accountRepository.findAccountByCustomerId(customerId);
        List<AccountDTOOut> result = new ArrayList<>();
        for (Account account : accounts) {
            result.add(convertToDTO(account));
        }
        return result;
    }

    public List<AccountDTOOut> getMyAccounts(Integer userId) {
        Customer customer = customerRepository.findCustomerByUserId(userId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        List<Account> accounts = accountRepository.findAccountByCustomerId(customer.getId());
        List<AccountDTOOut> result = new ArrayList<>();
        for (Account account : accounts) {
            result.add(convertToDTO(account));
        }
        return result;
    }

    @Transactional
    public void deposit(Integer userId, Integer accountId, Double amount) {
        if (amount == null) {
            throw new ApiException("amount can not be null");
        }
        if (amount <= 0) {
            throw new ApiException("amount must be positive");
        }

        Customer customer = customerRepository.findCustomerByUserId(userId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new ApiException("Account not found");
        }
        if (!account.getCustomer().getId().equals(customer.getId())) {
            throw new ApiException("You do not own this account");
        }
        if (!Boolean.TRUE.equals(account.getIsActive())) {
            throw new ApiException("Account is not active");
        }

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    @Transactional
    public void withdraw(Integer userId, Integer accountId, Double amount) {
        if (amount == null) {
            throw new ApiException("amount can not be null");
        }
        if (amount <= 0) {
            throw new ApiException("amount must be positive");
        }

        Customer customer = customerRepository.findCustomerByUserId(userId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new ApiException("Account not found");
        }
        if (!account.getCustomer().getId().equals(customer.getId())) {
            throw new ApiException("You do not own this account");
        }
        if (!Boolean.TRUE.equals(account.getIsActive())) {
            throw new ApiException("Account is not active");
        }

        if (amount > account.getBalance()) {
            throw new ApiException("Insufficient balance");
        }
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
    }

    @Transactional
    public void transfer(Integer userId, TransferDTOIn dto) {
        if (dto.getAmount() == null) {
            throw new ApiException("amount can not be null");
        }
        if (dto.getAmount() <= 0) {
            throw new ApiException("amount must be positive");
        }

        if (dto.getFromAccountId().equals(dto.getToAccountId())) {
            throw new ApiException("Source and destination accounts must be different");
        }

        Customer customer = customerRepository.findCustomerByUserId(userId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        Account source = accountRepository.findAccountById(dto.getFromAccountId());
        if (source == null) {
            throw new ApiException("Account not found");
        }
        if (!source.getCustomer().getId().equals(customer.getId())) {
            throw new ApiException("You do not own this account");
        }
        if (!Boolean.TRUE.equals(source.getIsActive())) {
            throw new ApiException("Account is not active");
        }

        Account destination = accountRepository.findAccountById(dto.getToAccountId());
        if (destination == null) {
            throw new ApiException("Account not found");
        }
        if (!Boolean.TRUE.equals(destination.getIsActive())) {
            throw new ApiException("Account is not active");
        }

        if (dto.getAmount() > source.getBalance()) {
            throw new ApiException("Insufficient balance");
        }

        source.setBalance(source.getBalance() - dto.getAmount());
        destination.setBalance(destination.getBalance() + dto.getAmount());
        accountRepository.save(source);
        accountRepository.save(destination);
    }

}
