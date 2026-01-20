package org.example.expensetracker.repository;

import org.example.expensetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    // For login & forgot password
    Optional<User> findByEmail(String email);

    // For OTP verification (step 2)
    Optional<User> findByEmailAndResetOtp(String email, String resetOtp);
}
