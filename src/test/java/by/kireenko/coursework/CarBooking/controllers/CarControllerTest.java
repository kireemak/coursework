package by.kireenko.coursework.CarBooking.controllers;

import by.kireenko.coursework.CarBooking.AbstractIntegreationTest;
import by.kireenko.coursework.CarBooking.models.Car;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest//(properties = "spring.cors.enabled=false")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CarControllerTest extends AbstractIntegreationTest {
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
    public void getAllCars_WhenUserIsAuthorized_ShouldReturnCarDtoList() throws Exception {
        UserDetails userDetails = userDetailsService.loadUserByUsername("testName");
        String token = jwtTokenUtils.generateToken(userDetails);

        mockMvc.perform(get("/api/cars")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(11)))
                .andExpect(jsonPath("$[10].id", is(2999999)))
                .andExpect(jsonPath("$[10].brand", is("testBrand3")))
                .andExpect(jsonPath("$[10].model", is("testModel3")));
    }

    @Test
    @Sql(scripts = "/insert-booking.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts= "/delete-test-data.sql" , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllCars_WhenUserIsNotAuthorized_ThrowsException() throws Exception {
        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(scripts = "/insert-booking.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getCarById_WhenCarExistsAndUserIsAuthorized_ShouldReturnCarDto() throws Exception {
        UserDetails userDetails = userDetailsService.loadUserByUsername("testName");
        String token = jwtTokenUtils.generateToken(userDetails);

        mockMvc.perform(get("/api/cars/{id}", 2999999)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2999999)))
                .andExpect(jsonPath("$.brand", is("testBrand3")))
                .andExpect(jsonPath("$.model", is("testModel3")));
    }

    @Test
    @Sql(scripts = "/insert-booking.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getCarById_WhenCarNotExistsAndUserIsAuthorized_ThrowsException() throws Exception {
        UserDetails userDetails = userDetailsService.loadUserByUsername("testName");
        String token = jwtTokenUtils.generateToken(userDetails);

        mockMvc.perform(get("/api/cars/{id}", 1234567)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    @Sql(scripts = "/insert-booking.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAvailableCars_WhenUserIsAuthorized_ShouldReturnCarDto() throws Exception {
        UserDetails userDetails = userDetailsService.loadUserByUsername("testName");
        String token = jwtTokenUtils.generateToken(userDetails);

        mockMvc.perform(get("/api/cars/available" )
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[4].id", is(1999999)))
                .andExpect(jsonPath("$[4].brand", is("testBrand2")))
                .andExpect(jsonPath("$[4].model", is("testModel2")))
                .andExpect(jsonPath("$[4].status", is("Available")));
    }

    @Test
    @Sql(scripts = "/insert-booking.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createCar_WhenUserIsAdmin_ShouldReturnCarDto() throws Exception {
        UserDetails userDetails = userDetailsService.loadUserByUsername("testName3");
        String token = jwtTokenUtils.generateToken(userDetails);

        Car car = new Car();
        car.setStatus("Available");
        car.setYear(2020);
        car.setBrand("testBrand1234");
        car.setModel("testModel1234");
        car.setRentalPrice(123.0);

        mockMvc.perform(post("/api/cars")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()));

        mockMvc.perform(get("/api/cars")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(12)));

        mockMvc.perform(get("/api/cars/available")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)));
    }

    @Test
    @Sql(scripts = "/insert-booking.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCar_WhenUserIsAdmin_ShouldReturnCarDto() throws Exception {
        UserDetails userDetails = userDetailsService.loadUserByUsername("testName3");
        String token = jwtTokenUtils.generateToken(userDetails);

        Car car = new Car();
        car.setId(2999999L);
        car.setStatus("Available");
        car.setYear(2024);
        car.setBrand("newBrand");
        car.setModel("newModel");
        car.setRentalPrice(222.0);

        mockMvc.perform(put("/api/cars/{id}", car.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is(car.getId().intValue())));

        mockMvc.perform(get("/api/cars/{id}", car.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(car.getId().intValue())))
                .andExpect(jsonPath("$.brand", is(car.getBrand())))
                .andExpect(jsonPath("$.model", is(car.getModel())))
                .andExpect(jsonPath("$.rentalPrice", is(car.getRentalPrice())))
                .andExpect(jsonPath("$.year", is(car.getYear())))
                .andExpect(jsonPath("$.status", is(car.getStatus())));
    }

    @Test
    @Sql(scripts = "/insert-booking.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteCar_WhenUserIsAdmin() throws Exception {
        UserDetails userDetails = userDetailsService.loadUserByUsername("testName3");
        String token = jwtTokenUtils.generateToken(userDetails);

        mockMvc.perform(delete("/api/cars/{id}", 2999999)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/cars/{id}", 2999999)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }
}
