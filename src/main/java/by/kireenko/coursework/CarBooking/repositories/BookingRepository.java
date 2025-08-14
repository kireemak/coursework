package by.kireenko.coursework.CarBooking.repositories;

import by.kireenko.coursework.CarBooking.models.Booking;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Booking> findAndLockById(Long id);

    List<Booking> findByUserId(Long id);
}
