package com.rkinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute User user, Model model) {
        userService.registerUser(user);
        model.addAttribute("msg", "Registration successful. Please login.");
        return "login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute User user, Model model, HttpSession session) {
        User validUser = userService.login(user.getEmail(), user.getPassword());
        if (validUser != null) {
            session.setAttribute("user", validUser);   // <-- change here
            return "redirect:/todos";
        }
        model.addAttribute("error", "Invalid email or password!");
        return "login";
    }

}
