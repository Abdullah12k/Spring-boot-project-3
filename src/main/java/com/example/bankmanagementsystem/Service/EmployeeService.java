package com.example.bankmanagementsystem.Service;

import com.example.bankmanagementsystem.Api.ApiException;
import com.example.bankmanagementsystem.DTO.DTOIn.RegisterEmployeeDTOIn;
import com.example.bankmanagementsystem.DTO.DTOOut.EmployeeDTOOut;
import com.example.bankmanagementsystem.Model.Employee;
import com.example.bankmanagementsystem.Model.User;
import com.example.bankmanagementsystem.Repository.AuthRepository;
import com.example.bankmanagementsystem.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    private EmployeeDTOOut convertToDTO(Employee employee) {
        User user = employee.getUser();
        return new EmployeeDTOOut(
                employee.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                employee.getPosition(),
                employee.getSalary()
        );
    }

    @Transactional
    public void registerEmployee(RegisterEmployeeDTOIn dto) {
        if (authRepository.existsByUsername(dto.getUsername())) {
            throw new ApiException("Username already used");
        }
        if (authRepository.existsByEmail(dto.getEmail())) {
            throw new ApiException("Email already used");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole("EMPLOYEE");
        authRepository.save(user);

        Employee employee = new Employee();
        employee.setPosition(dto.getPosition());
        employee.setSalary(dto.getSalary());
        employee.setUser(user);
        employeeRepository.save(employee);
        user.setEmployee(employee);
    }

    public List<EmployeeDTOOut> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeDTOOut> result = new ArrayList<>();
        for (Employee employee : employees) {
            result.add(convertToDTO(employee));
        }
        return result;
    }

    public EmployeeDTOOut getEmployeeById(Integer id) {
        Employee employee = employeeRepository.findEmployeeById(id);
        if (employee == null) {
            throw new ApiException("Employee not found");
        }
        return convertToDTO(employee);
    }

    public EmployeeDTOOut getMyProfile(Integer userId) {
        Employee employee = employeeRepository.findEmployeeByUserId(userId);
        if (employee == null) {
            throw new ApiException("Employee not found");
        }
        return convertToDTO(employee);
    }

    public void updateMyProfile(Integer userId, RegisterEmployeeDTOIn dto) {
        Employee oldEmployee = employeeRepository.findEmployeeByUserId(userId);
        if (oldEmployee == null) {
            throw new ApiException("Employee not found");
        }

        if (!oldEmployee.getUser().getEmail().equals(dto.getEmail())
                && authRepository.existsByEmail(dto.getEmail())) {
            throw new ApiException("Email already used");
        }

        oldEmployee.getUser().setName(dto.getName());
        oldEmployee.getUser().setEmail(dto.getEmail());
        oldEmployee.setPosition(dto.getPosition());
        oldEmployee.setSalary(dto.getSalary());
        employeeRepository.save(oldEmployee);
    }

    public void deleteMyProfile(Integer userId) {
        Employee employee = employeeRepository.findEmployeeByUserId(userId);
        if (employee == null) {
            throw new ApiException("Employee not found");
        }
        authRepository.delete(employee.getUser());
    }

    public void updateEmployee(Integer employeeId, RegisterEmployeeDTOIn dto) {
        Employee oldEmployee = employeeRepository.findEmployeeById(employeeId);
        if (oldEmployee == null) {
            throw new ApiException("Employee not found");
        }

        if (!oldEmployee.getUser().getEmail().equals(dto.getEmail())
                && authRepository.existsByEmail(dto.getEmail())) {
            throw new ApiException("Email already used");
        }

        oldEmployee.getUser().setName(dto.getName());
        oldEmployee.getUser().setEmail(dto.getEmail());
        oldEmployee.setPosition(dto.getPosition());
        oldEmployee.setSalary(dto.getSalary());
        employeeRepository.save(oldEmployee);
    }

    public void deleteEmployee(Integer employeeId) {
        Employee employee = employeeRepository.findEmployeeById(employeeId);
        if (employee == null) {
            throw new ApiException("Employee not found");
        }
        authRepository.delete(employee.getUser());
    }

}
