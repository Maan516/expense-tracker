package org.example.expensetracker.service;

import org.example.expensetracker.entity.User;
import org.example.expensetracker.service.EmailService;
import org.example.expensetracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import java.util.Random;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    //Inject PasswordEncoder
    public UserServiceImp(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    // ================= REGISTER =================
    @Override
    public User registerUser(User user) {

        // 🔐 ENCODE PASSWORD BEFORE SAVING
        String encodedPassword =
                passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    // ================= LOGIN =================
    @Override
    public User loginUser(String email, String password) {

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return null;
        }

        // 🔐 MATCH RAW PASSWORD WITH ENCODED PASSWORD
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }

        return user;
    }
    @Override
    public String changePassword(
            User user,
            String currentPassword,
            String newPassword,
            String confirmPassword
    ) {

        // 1️⃣ Check current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return "Current password is incorrect";
        }

        // 2️⃣ Check new password match
        if (!newPassword.equals(confirmPassword)) {
            return "New passwords do not match";
        }

        // 3️⃣ Password length check
        if (newPassword.length() < 6) {
            return "Password must be at least 6 characters";
        }

        // 4️⃣ Encode & save
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "SUCCESS";
    }
    // =================================================
    // STEP 1: SEND OTP
    // =================================================
    @Override
    public void sendPasswordResetOtp(String email) {

        Optional<User> userOpt = userRepository.findByEmail(email);

        //Do NOT expose whether email exists
        if (userOpt.isEmpty()) {
            return;
        }

        User user = userOpt.get();

        // Generate 6-digit OTP
        String otp = generateOtp();

        user.setResetOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

        userRepository.save(user);

        //TODO: Send email here
        emailService.sendOtpEmail(email, otp);

    }
    // =================================================
    // STEP 2: VERIFY OTP
    // =================================================
    @Override
    public boolean verifyPasswordResetOtp(String email, String otp) {

        Optional<User> userOpt =
                userRepository.findByEmailAndResetOtp(email, otp);

        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();

        // Check expiry
        if(user.getOtpExpiry() == null ||
                user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return false;
        }

        return true;
    }
    // =================================================
    // STEP 3: RESET PASSWORD
    // =================================================
    @Override
    public void resetPassword(
            String email,
            String newPassword,
            String confirmPassword
    ) {

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid request");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match");
        }

        User user = userOpt.get();

        // Encode & update password
        user.setPassword(passwordEncoder.encode(newPassword));

        // Clear OTP data
        user.setResetOtp(null);
        user.setOtpExpiry(null);

        userRepository.save(user);
    }
    // =================================================
    // OTP GENERATOR (PRIVATE)
    // =================================================
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
    @Override
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }


}
