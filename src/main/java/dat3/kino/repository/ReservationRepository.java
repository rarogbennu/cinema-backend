package dat3.kino.repository;

import dat3.kino.entity.Movie;
import dat3.kino.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository <Reservation, Integer> {
    Optional<Reservation> findById(int id);
}
