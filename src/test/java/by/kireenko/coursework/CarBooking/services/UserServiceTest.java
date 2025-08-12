package by.kireenko.coursework.CarBooking.services;

import by.kireenko.coursework.CarBooking.dto.UserDto;
import by.kireenko.coursework.CarBooking.error.ResourceNotFoundException;
import by.kireenko.coursework.CarBooking.models.Role;
import by.kireenko.coursework.CarBooking.models.User;
import by.kireenko.coursework.CarBooking.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    List<User> users;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void initializeUsers() {
        users = List.of(new User(1L, "testName1", "testEmail1", "testPhone1",
                        "testPassword1", List.of(new Role(1, "ROLE_USER")), List.of()),
                new User(2L, "testName2", "testEmail2", "testPhone2",
                        "testPassword2", List.of(new Role(1, "ROLE_USER")), List.of()),
                new User(3L, "testName3", "testEmail3", "testPhone3",
                        "testPassword3", List.of(new Role(1, "ROLE_USER")), List.of()),
                new User(4L, "testName4", "testEmail4", "testPhone4",
                        "testPassword4", List.of(new Role(1, "ROLE_USER")), List.of()),
                new User(5L, "testName5", "testEmail5", "testPhone5",
                        "testPassword5", List.of(new Role(2, "ROLE_ADMIN")), List.of()),
                new User(6L, "testName6", "testEmail6", "testPhone6",
                        "testPassword6", List.of(new Role(1, "ROLE_USER")), List.of()),
                new User(7L, "testName7", "testEmail7", "testPhone7",
                        "testPassword7", List.of(new Role(1, "ROLE_USER")), List.of()),
                new User(8L, "testName8", "testEmail8", "testPhone8",
                        "testPassword8", List.of(new Role(2, "ROLE_ADMIN")), List.of()));

        users.forEach(user -> user.setPassword(passwordEncoder.encode(user.getPassword())));
    }

    @Test
    public void getAllUsersDto_ReturnUsersDtoList() {
        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> userDtoList = userService.getAllUsersDto();

        assertThat(userDtoList).isNotNull();
        assertThat(userDtoList).isNotEmpty();
        assertThat(userDtoList.getFirst()).isInstanceOf(UserDto.class);
        assertThat(userDtoList).hasSize(8);
        assertThat(userDtoList.get(0).getName()).isEqualTo("testName1");
        assertThat(userDtoList.get(1).getName()).isEqualTo("testName2");
        assertThat(userDtoList.get(6).getName()).isEqualTo("testName7");
        assertThat(userDtoList.get(7).getName()).isEqualTo("testName8");
    }

    @Test
    public void getUserByName_WhenUserIsExist_ReturnUser() {
        when(userRepository.findByName("testName4")).thenReturn(users.stream()
                .filter(user -> user.getName().equals("testName4")).findFirst());

        User user = userService.getUserByName("testName4");

        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo("testName4");
        assertThat(user.getEmail()).isEqualTo("testEmail4");
        assertThat(user.getPhoneNumber()).isEqualTo("testPhone4");
        assertThat(user.getRoles()).isNotEmpty();
        assertThat(user.getRoles()).hasSize(1);
        assertThat(user.getRoles()).extracting("name").contains("ROLE_USER");
    }

    @Test
    public void getUserByName_WhenUserIsNotExist_ThrowsException() {
        when(userRepository.findByName("testName12345")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByName("testName12345"));
    }

    @Test
    public void getUserById_WhenUserIsExist_ReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(users.get(0)));

        User user = userService.getUserById(1L);

        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo("testName1");
        assertThat(user.getEmail()).isEqualTo("testEmail1");
        assertThat(user.getPhoneNumber()).isEqualTo("testPhone1");
        assertThat(user.getRoles()).isNotEmpty();
        assertThat(user.getRoles()).hasSize(1);
        assertThat(user.getRoles()).extracting("name").contains("ROLE_USER");
    }

    @Test
    public void getUserById_WhenUserIsNotExist_ThrowsException() {
        when(userRepository.findById(12345L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(12345L));
    }
}
