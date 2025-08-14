package by.kireenko.coursework.CarBooking.controllers;

import by.kireenko.coursework.CarBooking.AbstractIntegreationTest;
import by.kireenko.coursework.CarBooking.error.ResourceNotFoundException;
import by.kireenko.coursework.CarBooking.models.Booking;
import by.kireenko.coursework.CarBooking.models.Car;
import by.kireenko.coursework.CarBooking.services.BookingService;
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

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest//(properties = "spring.cors.enabled=false")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingControllerTest extends AbstractIntegreationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookingService bookingService;

    @Test
    @Sql(scripts = "/insert-booking.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBookingById_WhenBookingExistsAndUserIsOwner_ShouldReturnBooking() throws Exception {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testName");
        String token = jwtTokenUtils.generateToken(userDetails);

        mockMvc.perform(get("/api/bookings/{id}", 999999)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id", is(999999)))
                .andExpect(jsonPath("$.status", is("Created")));
    }

    @Test
    @Sql(scripts = "/insert-booking.sql", executionPhase =Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBookingById_WhenBookingExistsAndWithoutToken_ShouldThrowException() throws Exception {
        mockMvc.perform(get("/api/bookings/{id}", 999999))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(scripts = "/insert-booking.sql", executionPhase =Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBookingById_WhenBookingExistsAndUserIsNotOwner_ShouldThrowException() throws Exception {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testName2");
        String token = jwtTokenUtils.generateToken(userDetails);

        mockMvc.perform(get("/api/bookings/{id}", 999999)
                        .header("Authorization", "Bearer " + token))
                    .andExpect(status().isForbidden());
    }

    @Test
    @Sql(scripts = "/insert-booking.sql", executionPhase =Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBookingById_WhenBookingNotFound_ShouldThrowException() throws Exception {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testName");
        String token = jwtTokenUtils.generateToken(userDetails);

        mockMvc.perform(get("/api/bookings/{id}", 2000)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = "/insert-booking.sql", executionPhase =Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createBookingWithCheck_WhenCarIsAvailable_ShouldReturnBooking() throws Exception {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testName");
        String token = jwtTokenUtils.generateToken(userDetails);

        Booking booking = new Booking();
        Car car = new Car();
        car.setId(1999999L);
        car.setStatus("Available");
        booking.setCar(car);
        booking.setStartDate(LocalDate.now());
        booking.setEndDate(LocalDate.now().plusMonths(2));

        mockMvc.perform(post("/api/bookings/create-with-check")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("Created")));
    }

    @Test
    @Sql(scripts = "/insert-booking.sql", executionPhase =Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateBooking_WhenUserIsOwner_ShouldReturnBooking() throws Exception {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testName");
        String token = jwtTokenUtils.generateToken(userDetails);

        Booking booking = new Booking();
        LocalDate startDate = LocalDate.now().minusMonths(3);
        LocalDate endDate = LocalDate.now().plusMonths(3);

        booking.setStartDate(startDate);
        booking.setEndDate(endDate);

        mockMvc.perform(put("/api/bookings/{id}", 999999L)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("Created")))
                .andExpect(jsonPath("$.startDate", is(List.of(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth()))))
                .andExpect(jsonPath("$.endDate", is(List.of(endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth()))));
    }

    @Test
    @Sql(scripts = "/insert-booking.sql", executionPhase =Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteBooking_WhenUserIsOwner() throws Exception {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testName");
        String token = jwtTokenUtils.generateToken(userDetails);

        mockMvc.perform(delete("/api/bookings/{id}", 1999999L)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/bookings/{id}", 1999999L)
                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isNotFound());
    }
}
