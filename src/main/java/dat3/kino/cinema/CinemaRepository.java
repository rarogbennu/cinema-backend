package dat3.kino.cinema;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CinemaRepository extends JpaRepository<Cinema, Integer> {
    Optional<Cinema> findByName(String name);
}
