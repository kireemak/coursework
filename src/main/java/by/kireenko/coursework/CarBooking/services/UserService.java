package by.kireenko.coursework.CarBooking.services;

import by.kireenko.coursework.CarBooking.models.User;
import by.kireenko.coursework.CarBooking.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByName(String name) {
        return userRepository.findByName(name).orElse(null);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Transactional(readOnly = false)
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional(readOnly = false)
    public User updateUser(Long id, User updatedUser) {
        User existingUser = getUserById(id);
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        return userRepository.save(existingUser);
    }

    @Transactional(readOnly = false)
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
