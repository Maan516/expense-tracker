package org.example.expensetracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // ✅ Public routes (YOUR CONTROLLER URLs)
                        .requestMatchers(
                                "/",
                                "/users/login",
                                "/users/register",
                                "/users/forgot-password",
                                "/users/verify-otp"
                        ).permitAll()

                        // ✅ Static files
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**").permitAll()

                        // ✅ Admin-only
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")

                        // ✅ User + Admin
                        .requestMatchers("/dashboard/**", "/expenses/**", "/categories/**")
                        .hasAnyAuthority("ADMIN", "USER")

                        // ✅ Everything else
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/users/login")        // ✅ your GET login page
                        .loginProcessingUrl("/login")     // ✅ POST URL for login form
                        .successHandler(successHandler()) // ✅ redirect ADMIN/USER
                        .failureUrl("/users/login?error=true")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/users/login?logout=true")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ADMIN"));
            if (isAdmin) {
                response.sendRedirect("/admin/dashboard?success=true");
            } else {
                response.sendRedirect("/dashboard?success=true");
            }

        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
