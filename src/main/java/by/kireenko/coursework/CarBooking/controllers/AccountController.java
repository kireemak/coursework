package by.kireenko.coursework.CarBooking.controllers;

import by.kireenko.coursework.CarBooking.dto.UserDto;
import by.kireenko.coursework.CarBooking.models.User;
import by.kireenko.coursework.CarBooking.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Account Controller", description = "Endpoints for managing the current user's account")
public class AccountController {
    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get current user info", description = "Returns information about the currently authenticated user.")
    public UserDto getCurrentUserInfo(Principal principal) {
        String username = principal.getName();
        return new UserDto(userService.getUserByName(username));
    }
}
