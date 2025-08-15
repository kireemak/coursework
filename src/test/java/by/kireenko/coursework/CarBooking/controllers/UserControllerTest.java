package by.kireenko.coursework.CarBooking.controllers;

import by.kireenko.coursework.CarBooking.AbstractIntegreationTest;
import by.kireenko.coursework.CarBooking.dto.UserDto;
import by.kireenko.coursework.CarBooking.models.Role;
import by.kireenko.coursework.CarBooking.models.User;
import by.kireenko.coursework.CarBooking.services.CustomUserDetailsService;
import by.kireenko.coursework.CarBooking.utils.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest//(properties = "spring.cors.enabled=false")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest extends AbstractIntegreationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql(scripts = "/insert-booking.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllUsers_WhenClientIsAdmin_ShouldReturnAllUserDtoList() throws Exception {
        UserDetails userDetails = userDetailsService.loadUserByUsername("testName3");
        String token = jwtTokenUtils.generateToken(userDetails);

        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(13)));
    }

    @Test
    @Sql(scripts = "/insert-booking.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts= "/delete-test-data.sql" , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllUsers_WhenClientIsNotAuthorized_ThrowsException() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(scripts = "/insert-booking.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getUserById_WhenUserExistsAndClientIsAdmin_ShouldReturnUserDto() throws Exception {
        UserDetails userDetails = userDetailsService.loadUserByUsername("testName3");
        String token = jwtTokenUtils.generateToken(userDetails);

        mockMvc.perform(get("/api/users/{id}", 2999999)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2999999)))
                .andExpect(jsonPath("$.name", is("testName3")))
                .andExpect(jsonPath("$.phoneNumber", is("testPhone3")))
                .andExpect(jsonPath("$.email", is("testEmail3")));
    }

    @Test
    @Sql(scripts = "/insert-booking.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getUserById_WhenUserNotExistsAndClientIsAdmin_ThrowsException() throws Exception {
        UserDetails userDetails = userDetailsService.loadUserByUsername("testName3");
        String token = jwtTokenUtils.generateToken(userDetails);

        mockMvc.perform(get("/api/users/{id}", 1234567)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    @Sql(scripts = "/insert-booking.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createUser_WhenClientIsAdmin_ShouldReturnUserDto() throws Exception {
        UserDetails userDetails = userDetailsService.loadUserByUsername("testName3");
        String token = jwtTokenUtils.generateToken(userDetails);

        User user = new User();
        user.setName("testName12345");
        user.setPhoneNumber("34525462484");
        user.setEmail("testEmail@test.ru");
        user.setRoles(List.of(new Role(1, "ROLE_USER")));
        user.setPassword("password");

        mockMvc.perform(post("/api/users")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()));

        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(14)));
    }

    @Test
    @Sql(scripts = "/insert-booking.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateUser_WhenClientIsAdmin_ShouldReturnUserDto() throws Exception {
        UserDetails userDetails = userDetailsService.loadUserByUsername("testName3");
        String token = jwtTokenUtils.generateToken(userDetails);

        UserDto userDto = new UserDto(999999L, "newName", "newEmail", "newPhoneNumber");

        mockMvc.perform(put("/api/users/{id}", userDto.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is(userDto.getId().intValue())));

        mockMvc.perform(get("/api/users/{id}", userDto.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.phoneNumber", is(userDto.getPhoneNumber())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    @Sql(scripts = "/insert-booking.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteUser_WhenClientIsAdmin() throws Exception {
        UserDetails userDetails = userDetailsService.loadUserByUsername("testName3");
        String token = jwtTokenUtils.generateToken(userDetails);

        mockMvc.perform(delete("/api/users/{id}", 2999999)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users/{id}", 2999999)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }
}
