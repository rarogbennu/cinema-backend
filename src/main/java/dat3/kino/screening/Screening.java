package dat3.kino.screening;

import dat3.kino.cinema.Cinema;
import dat3.kino.screen.Screen;
import dat3.kino.seat.Seat;
import dat3.kino.movie.Movie;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "is_3d")
    private boolean is3D;

    public Screening(LocalDateTime date, Movie movie, Cinema cinema, Screen screen) {
        this.date = date;
        this.movie = movie;
        this.cinema = cinema;
        this.screen = screen;
        this.is3D = is3D;
    }

    // Method to check if a given seat ID belongs to the associated screen
    public boolean isSeatValidForScreen(int seatId) {
        return getScreen().getSeats().stream()
                .map(Seat::getId)
                .anyMatch(id -> Objects.equals(id, seatId));
    }
}
