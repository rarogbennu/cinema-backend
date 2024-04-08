package dat3.kino.seat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository <Seat,Integer> {
    List<Seat> findByScreenId(int screenId);
}
