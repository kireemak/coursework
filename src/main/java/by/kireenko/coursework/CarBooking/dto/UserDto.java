package by.kireenko.coursework.CarBooking.dto;

import by.kireenko.coursework.CarBooking.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "DTO for user data")
public class UserDto {
    @Schema(description = "User ID", example = "1")
    private Long id;
    @Schema(description = "Username", example = "John Doe")
    private String name;
    @Schema(description = "User email", example = "john.doe@example.com")
    private String email;
    @Schema(description = "User phone number", example = "+1234567890")
    private String phoneNumber;

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
    }
}
