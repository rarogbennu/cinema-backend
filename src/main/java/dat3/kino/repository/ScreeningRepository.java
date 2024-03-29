package dat3.kino.repository;

import dat3.kino.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening, Integer> {
    List<Screening> findByCinemaId(int cinemaId);
}
