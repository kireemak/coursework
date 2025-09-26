package by.kireenko.coursework.CarBooking.services;

import by.kireenko.coursework.CarBooking.dto.UpdateUserRequestDto;
import by.kireenko.coursework.CarBooking.dto.UserDto;
import by.kireenko.coursework.CarBooking.error.ResourceNotFoundException;
import by.kireenko.coursework.CarBooking.models.Booking;
import by.kireenko.coursework.CarBooking.models.User;
import by.kireenko.coursework.CarBooking.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService self;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, @Lazy UserService self) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.self = self;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<UserDto> getAllUsersDto() {
        List<UserDto> dtoList = new ArrayList<>();
        getAllUsers().forEach(user -> dtoList.add(new UserDto(user)));
        return dtoList;
    }

    @Cacheable(value = "users", key = "#name")
    public User getUserByName(String name) {
        return userRepository.findByName(name).orElseThrow(() -> {
            log.warn("User with name {} not found", name);
            return new ResourceNotFoundException("User", "name", name);
        });
    }

    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User with id {} not found", id);
                    return new ResourceNotFoundException("User", "id", id);
                });
    }

    @Transactional(readOnly = false)
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional(readOnly = false)
    @Caching(
            evict = {
                    @CacheEvict(value = "users", key = "#root.target.getUserById(#userId).name", beforeInvocation = true)
            },
            put = {
                    @CachePut(value = "users", key = "#result.id"),
                    @CachePut(value = "users", key = "#result.name")
            }
    )
    public User updateUser(Long userId, UpdateUserRequestDto userRequestDto) {
        User existingUser = self.getUserById(userId);
        if (userRequestDto.getName() != null)
            existingUser.setName(userRequestDto.getName());
        if (userRequestDto.getEmail() != null)
            existingUser.setEmail(userRequestDto.getEmail());
        if (userRequestDto.getPhoneNumber() != null)
            existingUser.setPhoneNumber(userRequestDto.getPhoneNumber());
        return userRepository.save(existingUser);
    }

    @Transactional(readOnly = false)
    @Caching(
            evict = {
                    @CacheEvict(value = "users", key = "#id", beforeInvocation = true),
                    @CacheEvict(value = "users", key = "#root.target.getUserById(#id).name", beforeInvocation = true)
            }
    )
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return self.getUserByName(username);
    }

    public List<Booking> getCurrentUserBookingList(User user) {
        User currUser = self.getUserById(user.getId());
        return currUser.getBookings();
    }
}
