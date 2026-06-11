package com.example.bankmanagementsystem.Controller;

import com.example.bankmanagementsystem.Api.ApiResponse;
import com.example.bankmanagementsystem.DTO.DTOIn.RegisterEmployeeDTOIn;
import com.example.bankmanagementsystem.Model.User;
import com.example.bankmanagementsystem.Service.EmployeeService;
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
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/register")
    public ResponseEntity<?> registerEmployee(@RequestBody @Valid RegisterEmployeeDTOIn dto) {
        employeeService.registerEmployee(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Employee registered successfully"));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/get/{employeeId}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(employeeService.getEmployeeById(employeeId));
    }

    @GetMapping("/my-profile")
    public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.getMyProfile(user.getId()));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateMyProfile(@AuthenticationPrincipal User user,@RequestBody @Valid RegisterEmployeeDTOIn dto) {
        employeeService.updateMyProfile(user.getId(), dto);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Employee updated successfully"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMyProfile(@AuthenticationPrincipal User user) {
        employeeService.deleteMyProfile(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Employee deleted successfully"));
    }

    @PutMapping("/update/{employeeId}")
    public ResponseEntity<?> updateEmployee(@PathVariable Integer employeeId, @RequestBody @Valid RegisterEmployeeDTOIn dto) {
        employeeService.updateEmployee(employeeId, dto);
        return ResponseEntity.ok(new ApiResponse("Employee updated successfully"));
    }

    @DeleteMapping("/delete/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Integer employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok(new ApiResponse("Employee deleted successfully"));
    }
}
