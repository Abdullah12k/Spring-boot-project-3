package com.example.bankmanagementsystem.Service;

import com.example.bankmanagementsystem.Api.ApiException;
import com.example.bankmanagementsystem.DTO.DTOIn.RegisterCustomerDTOIn;
import com.example.bankmanagementsystem.DTO.DTOOut.AccountDTOOut;
import com.example.bankmanagementsystem.DTO.DTOOut.CustomerDTOOut;
import com.example.bankmanagementsystem.Model.Account;
import com.example.bankmanagementsystem.Model.Customer;
import com.example.bankmanagementsystem.Model.User;
import com.example.bankmanagementsystem.Repository.AccountRepository;
import com.example.bankmanagementsystem.Repository.AuthRepository;
import com.example.bankmanagementsystem.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AuthRepository authRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    private CustomerDTOOut convertToDTO(Customer customer) {
        User user = customer.getUser();
        return new CustomerDTOOut(
                customer.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                customer.getPhoneNumber()
        );
    }

    private AccountDTOOut convertAccountToDTO(Account account) {
        return new AccountDTOOut(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getIsActive(),
                account.getCustomer().getId()
        );
    }

    @Transactional
    public void registerCustomer(RegisterCustomerDTOIn dto) {
        if (authRepository.existsByUsername(dto.getUsername())) {
            throw new ApiException("Username already used");
        }
        if (authRepository.existsByEmail(dto.getEmail())) {
            throw new ApiException("Email already used");
        }
        if (customerRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new ApiException("Phone number already used");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole("CUSTOMER");
        authRepository.save(user);

        Customer customer = new Customer();
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setUser(user);
        customerRepository.save(customer);
        user.setCustomer(customer);
    }

    public List<CustomerDTOOut> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTOOut> result = new ArrayList<>();
        for (Customer customer : customers) {
            result.add(convertToDTO(customer));
        }
        return result;
    }

    public CustomerDTOOut getCustomerById(Integer id) {
        Customer customer = customerRepository.findCustomerById(id);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        return convertToDTO(customer);
    }

    public CustomerDTOOut getMyProfile(Integer userId) {
        Customer customer = customerRepository.findCustomerByUserId(userId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        return convertToDTO(customer);
    }

    public void updateMyProfile(Integer userId, RegisterCustomerDTOIn dto) {
        Customer oldCustomer = customerRepository.findCustomerByUserId(userId);
        if (oldCustomer == null) {
            throw new ApiException("Customer not found");
        }

        if (!oldCustomer.getUser().getEmail().equals(dto.getEmail())
                && authRepository.existsByEmail(dto.getEmail())) {
            throw new ApiException("Email already used");
        }
        if (!oldCustomer.getPhoneNumber().equals(dto.getPhoneNumber())
                && customerRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new ApiException("Phone number already used");
        }

        oldCustomer.getUser().setName(dto.getName());
        oldCustomer.getUser().setEmail(dto.getEmail());
        oldCustomer.setPhoneNumber(dto.getPhoneNumber());
        customerRepository.save(oldCustomer);
    }

    public void deleteMyProfile(Integer userId) {
        Customer customer = customerRepository.findCustomerByUserId(userId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        authRepository.delete(customer.getUser());
    }

    public List<AccountDTOOut> getMyAccounts(Integer userId) {
        Customer customer = customerRepository.findCustomerByUserId(userId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        List<Account> accounts = accountRepository.findAccountByCustomerId(customer.getId());
        List<AccountDTOOut> result = new ArrayList<>();
        for (Account account : accounts) {
            result.add(convertAccountToDTO(account));
        }
        return result;
    }

    public void updateCustomer(Integer customerId, RegisterCustomerDTOIn dto) {
        Customer oldCustomer = customerRepository.findCustomerById(customerId);
        if (oldCustomer == null) {
            throw new ApiException("Customer not found");
        }

        if (!oldCustomer.getUser().getEmail().equals(dto.getEmail())
                && authRepository.existsByEmail(dto.getEmail())) {
            throw new ApiException("Email already used");
        }
        if (!oldCustomer.getPhoneNumber().equals(dto.getPhoneNumber())
                && customerRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new ApiException("Phone number already used");
        }

        oldCustomer.getUser().setName(dto.getName());
        oldCustomer.getUser().setEmail(dto.getEmail());
        oldCustomer.setPhoneNumber(dto.getPhoneNumber());
        customerRepository.save(oldCustomer);
    }

    public void deleteCustomer(Integer customerId) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        authRepository.delete(customer.getUser());
    }

    public List<AccountDTOOut> getCustomerAccounts(Integer customerId) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        List<Account> accounts = accountRepository.findAccountByCustomerId(customerId);
        List<AccountDTOOut> result = new ArrayList<>();
        for (Account account : accounts) {
            result.add(convertAccountToDTO(account));
        }
        return result;
    }

}
