package by.kireenko.coursework.CarBooking.services;

import by.kireenko.coursework.CarBooking.dto.JwtRequest;
import by.kireenko.coursework.CarBooking.dto.JwtResponse;
import by.kireenko.coursework.CarBooking.dto.RegistrationUserDto;
import by.kireenko.coursework.CarBooking.error.AppError;
import by.kireenko.coursework.CarBooking.error.MismatchedPasswordsException;
import by.kireenko.coursework.CarBooking.error.UserAlreadyExistsException;
import by.kireenko.coursework.CarBooking.models.User;
import by.kireenko.coursework.CarBooking.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthToken(JwtRequest jwtRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getName(), jwtRequest.getPassword()));
        } catch (BadCredentialsException e) {
            log.warn("Authentication failed for the user: {}", jwtRequest.getName());
            throw new BadCredentialsException("Incorrect username or password");
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtRequest.getName());
        String token = jwtTokenUtils.generateToken(userDetails);
        log.info("Token generated for user: {}", jwtRequest.getName());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> createNewUser(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            log.warn("Password mismatch for user registration: {}", registrationUserDto.getName());
            throw new MismatchedPasswordsException("Passwords do not match");
        }
        if (customUserDetailsService.loadUserByUsername(registrationUserDto.getName()) != null) {
            log.warn("Username already exist for user: {}", registrationUserDto.getName());
            throw new UserAlreadyExistsException("Username already in use");
        }

        User user = customUserDetailsService.createNewUser(registrationUserDto);
        log.info("Created new user: {}", user.getName());
        return ResponseEntity.ok(user);
    }
}
