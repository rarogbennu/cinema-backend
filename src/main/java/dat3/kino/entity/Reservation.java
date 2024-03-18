package dat3.kino.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumn(name = "screening_id")
    private Screening screening;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "total_reservation_id")
    private TotalReservation totalReservation; // Reference to the Order entity

    private String dummyUser;

    private double price;

    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime edited;

    public Reservation(Screening screening, Seat seat, String dummyUser, double price) {
        this.screening = screening;
        this.seat = seat;
        this.dummyUser = dummyUser;
        this.price = price;
    }
}


//    @ManyToOne
//    @JoinColumn(name = "user_id") // Assuming you have a User entity
//    private User user;
