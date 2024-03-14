package dat3.kino.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "screening")
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;

    @ManyToOne
    @JoinColumn(name = "screen_id")
    private Screen screen;

    public Screening(LocalDateTime date, Movie movie, Cinema cinema, Screen screen) {
        this.date = date;
        this.movie = movie;
        this.cinema = cinema;
        this.screen = screen;
    }

    // Method to check if a given seat ID belongs to the associated screen
    public boolean isSeatValidForScreen(int seatId) {
        return getScreen().getSeats().stream()
                .map(Seat::getId)
                .anyMatch(id -> Objects.equals(id, seatId));
    }
}
