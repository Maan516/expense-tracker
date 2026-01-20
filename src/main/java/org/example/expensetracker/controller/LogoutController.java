package org.example.expensetracker.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
public class LogoutController {
    @GetMapping("/logout")
    public String logout(HttpSession session,
                         RedirectAttributes redirectAttributes) {

        session.invalidate();

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Logged out successfully"
        );

        return "redirect:/users/login";
    }


}
