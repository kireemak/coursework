package by.kireenko.coursework.CarBooking.controllers;

import by.kireenko.coursework.CarBooking.AbstractIntegreationTest;
import by.kireenko.coursework.CarBooking.models.Booking;
import by.kireenko.coursework.CarBooking.models.Car;
import by.kireenko.coursework.CarBooking.models.User;
import by.kireenko.coursework.CarBooking.services.CarService;
import by.kireenko.coursework.CarBooking.services.CustomUserDetailsService;
import by.kireenko.coursework.CarBooking.services.UserService;
import by.kireenko.coursework.CarBooking.utils.JwtTokenUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.cors.enabled=false")
@AutoConfigureMockMvc
public class BookingControllerTest extends AbstractIntegreationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql("/insert-booking.sql")
    public void getBookingById_WhenBookingExistsAndUserIsOwner_ShouldReturnBooking() throws Exception {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testName");
        String token = jwtTokenUtils.generateToken(userDetails);

        mockMvc.perform(get("/api/bookings/{id}", 999)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id", is(999)))
                .andExpect(jsonPath("$.status", is("Created")));
    }

    @Test
    @Sql("/insert-booking.sql")
    public void getBookingById_WhenBookingExistsAndWithoutToken_ShouldThrowException() throws Exception {
        mockMvc.perform(get("/api/bookings/{id}", 999))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql("/insert-booking.sql")
    public void getBookingById_WhenBookingExistsAndUserIsNotOwner_ShouldThrowException() throws Exception {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testName2");
        String token = jwtTokenUtils.generateToken(userDetails);

        mockMvc.perform(get("/api/bookings/{id}", 999)
                        .header("Authorization", "Bearer " + token))
                    .andExpect(status().isForbidden());
    }

    @Test
    @Sql("/insert-booking.sql")
    public void getBookingById_WhenBookingNotFound_ShouldThrowException() throws Exception {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testName");
        String token = jwtTokenUtils.generateToken(userDetails);

        mockMvc.perform(get("/api/bookings/{id}", 2000)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql("/insert-booking.sql")
    public void createBookingWithCheck_WhenCarIsAvailable_ShouldReturnBooking() throws Exception {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testName");
        String token = jwtTokenUtils.generateToken(userDetails);

        Booking booking = new Booking();
        Car car = new Car();
        car.setId(1999L);
        booking.setCar(car);
        booking.setStartDate(LocalDate.now());
        booking.setEndDate(LocalDate.now().plusMonths(2));

        mockMvc.perform(post("/api/bookings/create-with-check")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("Created")));
    }
}
