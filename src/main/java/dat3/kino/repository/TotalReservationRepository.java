package dat3.kino.repository;

import dat3.kino.entity.TotalReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TotalReservationRepository extends JpaRepository <TotalReservation, Integer> {
}
