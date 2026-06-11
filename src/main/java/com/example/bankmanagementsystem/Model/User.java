package com.example.bankmanagementsystem.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    // Admin accounts are managed separately from the registration flow
    // to preserve role integrity and maintain controlled system administration
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "username can not be empty")
    @Size(min = 4, max = 10, message = "username length must be between 4 and 10 characters")
    @Column(nullable = false, unique = true)
    private String username;

    @NotEmpty(message = "password can not be empty")
    @Size(min = 6, message = "password can not be less than 6 characters")
    @Column(nullable = false)
    private String password;

    @NotEmpty(message = "name can not be empty")
    @Size(min = 2, max = 20, message = "name length must be between 2 and 20 characters")
    @Column(nullable = false)
    private String name;

    @NotEmpty(message = "email can not be empty")
    @Email(message = "email must be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "(?)CUSTOMER|EMPLOYEE|ADMIN", message = "role must be CUSTOMER, EMPLOYEE, or ADMIN")
    @Column(nullable = false)
    private String role;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Customer customer;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Employee employee;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
