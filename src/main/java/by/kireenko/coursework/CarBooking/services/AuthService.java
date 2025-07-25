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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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
            throw new BadCredentialsException("Неправильный логин или пароль");
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtRequest.getName());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> createNewUser(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            throw new MismatchedPasswordsException("Пароли не совпадают");
        }
        if (customUserDetailsService.loadUserByUsername(registrationUserDto.getName()) != null) {
            throw new UserAlreadyExistsException("Пользователь с указанным именем уже существует");
        }

        User user = customUserDetailsService.createNewUser(registrationUserDto);
        return ResponseEntity.ok(user);
    }
}
