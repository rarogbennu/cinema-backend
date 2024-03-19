package dat3.kino.repository;

import dat3.kino.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository <Booking, Integer> {
}
