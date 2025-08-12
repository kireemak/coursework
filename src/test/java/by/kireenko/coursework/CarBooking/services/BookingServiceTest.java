package by.kireenko.coursework.CarBooking.services;

import by.kireenko.coursework.CarBooking.error.ResourceNotFoundException;
import by.kireenko.coursework.CarBooking.models.Booking;
import by.kireenko.coursework.CarBooking.models.Role;
import by.kireenko.coursework.CarBooking.models.User;
import by.kireenko.coursework.CarBooking.repositories.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock private BookingRepository bookingRepository;
    @Mock private CarService carService;
    @Mock private UserService userService;

    @InjectMocks
    private BookingService bookingService;

    @Test
    public void getBookingById_WhenBookingExistsAndUserIsOwner_ShouldReturnBooking() {
        User user = new User();
        user.setId(10L);

        Booking booking = new Booking();
        booking.setId(11L);
        booking.setUser(user);

        when(userService.getCurrentAuthenticatedUser()).thenReturn(user);
        when(bookingRepository.findById(11L)).thenReturn(Optional.of(booking));

        Booking result = bookingService.getBookingById(11L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(11L);
        assertThat(result.getUser()).isEqualTo(user);
        verify(bookingRepository).findById(11L);
    }

    @Test
    public void getBookingById_WhenBookingNotFound_ShouldThrowException() {
        User user = new User();
        user.setId(10L);
        when(userService.getCurrentAuthenticatedUser()).thenReturn(user);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.getBookingById(11L));

        verify(bookingRepository).findById(11L);
    }

    @Test
    public void getBookingById_WhenUserIsNotOwnerOrAdmin_ShouldThrowException() {
        User clientUser = new User();
        User ownerUser = new User();
        clientUser.setId(22L);
        clientUser.setRoles(List.of(new Role(1, "ROLE_USER")));
        ownerUser.setId(10L);
        ownerUser.setRoles(List.of(new Role(1, "ROLE_USER")));

        Booking booking = new Booking();
        booking.setId(11L);
        booking.setUser(ownerUser);

        when(userService.getCurrentAuthenticatedUser()).thenReturn(clientUser);
        when(bookingRepository.findById(11L)).thenReturn(Optional.of(booking));

        assertThrows(AccessDeniedException.class, () -> bookingService.getBookingById(11L));

        verify(bookingRepository).findById(11L);
    }

    @Test
    public void getBookingById_WhenBookingExistAndUserIsAdmin_ShouldReturnBooking() {
        User adminUser = new User();
        User ownerUser = new User();
        adminUser.setId(22L);
        Role adminRole = new Role(2, "ROLE_ADMIN");
        adminUser.setRoles(List.of(adminRole));
        ownerUser.setId(10L);

        Booking booking = new Booking();
        booking.setId(11L);
        booking.setUser(ownerUser);

        when(userService.getCurrentAuthenticatedUser()).thenReturn(adminUser);
        when(bookingRepository.findById(11L)).thenReturn(Optional.of(booking));

        Booking result = bookingService.getBookingById(11L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(11L);
        assertThat(result.getUser()).isEqualTo(ownerUser);
        verify(bookingRepository).findById(11L);
    }
}
