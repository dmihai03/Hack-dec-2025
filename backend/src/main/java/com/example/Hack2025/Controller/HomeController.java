package com.example.Hack2025.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.Hack2025.Entities.User;
import com.example.Hack2025.Repositories.UserRepository;


@Controller
public class HomeController {
    @GetMapping("/")
    @ResponseBody
    public String hello() {
        return new String("Hello from home!");
    }

    @PostMapping("/user/profile")
    public String getUserProfile(@RequestBody String userID) {
        User user = new UserRepository().getUserById(Long.parseLong(userID)).orElse(null);

        return user.toString();
    }
    
}
