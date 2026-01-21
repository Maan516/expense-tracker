package org.example.expensetracker.config;

import org.example.expensetracker.entity.Role;
import org.example.expensetracker.entity.User;
import org.example.expensetracker.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createAdmin(UserRepository userRepository) {
        return args -> {

            if (userRepository.findByEmail("admin@expensetracker.com").isEmpty()) {

                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@expensetracker.com");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);

                System.out.println("✅ Admin user created");
            }
        };
    }
}
