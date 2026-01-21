package org.example.expensetracker.service;

import org.example.expensetracker.entity.User;

public interface UserService {
    User registerUser(User user);


    User loginUser(String email, String password);

    String changePassword(
            User user,
            String currentPassword,
            String newPassword,
            String confirmPassword
    );
    //Send OTP to email
    void sendPasswordResetOtp(String email);

    //Verify OTP
    boolean verifyPasswordResetOtp(String email, String otp);

    //Reset password
    void resetPassword(
            String email,
            String newPassword,
            String confirmPassword
    );



}
