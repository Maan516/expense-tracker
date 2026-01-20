package org.example.expensetracker.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.expensetracker.entity.User;
import org.example.expensetracker.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // SHOW REGISTER PAGE
    @GetMapping("register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("register")
    public String registerUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            Model model) {

        // If validation fails → stay on register page
        if (result.hasErrors()) {
            return "register";
        }

        // Save user to database
        userService.registerUser(user);

        // Success message for same page
        model.addAttribute(
                "successMessage",
                "Registration successful! You can now login."
        );

        // Clear form (new empty object)
        model.addAttribute("user", new User());

        //  Stay on register page
        return "register";
    }


    // SHOW LOGIN PAGE
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }
    @PostMapping("/login")
    public String loginUser(
            @ModelAttribute("user") User user,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {

        User loggedInUser =
                userService.loginUser(user.getEmail(), user.getPassword());

        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Invalid email or password"
            );
            return "redirect:/users/login";
        }

        // Save logged-in user in session (correct usage)
        session.setAttribute("loggedInUser", loggedInUser);

        // Flash message for dashboard toast
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Login successful! Welcome back."
        );

        return "redirect:/dashboard";
    }
    // ===============================
    // FORGOT PASSWORD - STEP 1
    // ===============================
    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String sendOtp(
            @RequestParam("email") String email,
            RedirectAttributes redirectAttributes
    ) {

        userService.sendPasswordResetOtp(email);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "If the email exists, an OTP has been sent."
        );

        return "redirect:/users/verify-otp?email=" + email;
    }

    // ===============================
    // FORGOT PASSWORD - STEP 2
    // ===============================
    @GetMapping("/verify-otp")
    public String showVerifyOtpPage(
            @RequestParam("email") String email,
            Model model
    ) {
        model.addAttribute("email", email);
        return "verify-otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtpAndResetPassword(
            @RequestParam("email") String email,
            @RequestParam("otp") String otp,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            RedirectAttributes redirectAttributes
    ) {

        boolean valid =
                userService.verifyPasswordResetOtp(email, otp);

        if (!valid) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Invalid or expired OTP"
            );
            return "redirect:/users/verify-otp?email=" + email;
        }

        userService.resetPassword(email, newPassword, confirmPassword);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Password reset successful. Please login."
        );

        return "redirect:/users/login";
    }
}





