package com.example.bankmanagementsystem.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class ConfigSecurity {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/customer/register")
                        .permitAll()

                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/employee/register")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/customer/my-profile",
                                "/api/v1/customer/my-accounts",
                                "/api/v1/account/my-accounts")
                        .hasRole("CUSTOMER")

                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/employee/my-profile")
                        .hasRole("EMPLOYEE")

                        .requestMatchers(HttpMethod.PUT,
                                "/api/v1/customer/update")
                        .hasRole("CUSTOMER")

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/v1/customer/delete")
                        .hasRole("CUSTOMER")

                        .requestMatchers(HttpMethod.PUT,
                                "/api/v1/employee/update")
                        .hasRole("EMPLOYEE")

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/v1/employee/delete")
                        .hasRole("EMPLOYEE")

                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/customer/get",
                                "/api/v1/customer/get/**",
                                "/api/v1/customer/accounts/**",
                                "/api/v1/employee/get",
                                "/api/v1/employee/get/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT,
                                "/api/v1/customer/update/**",
                                "/api/v1/employee/update/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/v1/customer/delete/**",
                                "/api/v1/employee/delete/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/account/create/**")
                        .hasAnyRole("EMPLOYEE", "ADMIN")

                        .requestMatchers(HttpMethod.PUT,
                                "/api/v1/account/activate/**",
                                "/api/v1/account/block/**",
                                "/api/v1/account/update/**")
                        .hasAnyRole("EMPLOYEE", "ADMIN")

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/v1/account/delete/**")
                        .hasAnyRole("EMPLOYEE", "ADMIN")

                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/account/details/**",
                                "/api/v1/account/customer/**")
                        .hasAnyRole("EMPLOYEE", "ADMIN")

                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/account/deposit/**",
                                "/api/v1/account/withdraw/**",
                                "/api/v1/account/transfer")
                        .hasRole("CUSTOMER")

                        .anyRequest()
                        .denyAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                )

                .httpBasic(httpBasic -> {
                });

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
