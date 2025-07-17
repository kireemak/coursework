package by.kireenko.coursework.CarBooking.controllers;

import by.kireenko.coursework.CarBooking.dto.UserDto;
import by.kireenko.coursework.CarBooking.models.User;
import by.kireenko.coursework.CarBooking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsersDto();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return new UserDto(userService.getUserById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public UserDto createUser(@RequestBody User user) {
        return new UserDto(userService.createUser(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody User user) {
        return new UserDto(userService.updateUser(id, user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
