package by.kireenko.coursework.CarBooking.controllers;

import by.kireenko.coursework.CarBooking.dto.UserDto;
import by.kireenko.coursework.CarBooking.models.User;
import by.kireenko.coursework.CarBooking.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {
    private final UserService userService;

    @GetMapping("/me")
    public UserDto getCurrentUserInfo(Principal principal) {
        String username = principal.getName();
        return new UserDto(userService.getUserByName(username));
    }
}
