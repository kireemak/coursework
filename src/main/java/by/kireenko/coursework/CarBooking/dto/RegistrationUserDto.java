package by.kireenko.coursework.CarBooking.dto;

import lombok.Data;

@Data
public class RegistrationUserDto {
    private String name;
    private String password;
    private String confirmPassword;
    private String email;
    private String phone;
}
