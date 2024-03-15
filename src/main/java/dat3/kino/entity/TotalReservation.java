package dat3.kino.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "total_reservation")
public class TotalReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "totalReservation", fetch = FetchType.EAGER)
    private List<Reservation> reservations;

    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime edited;

    //number of reservations

    //total price

    public TotalReservation() {
        this.reservations = new ArrayList<>(); // Initialize the reservations list
    }

    public void addReservation(Reservation reservation) {
        // Add the reservation to the list of reservations
        reservations.add(reservation);
        // Set the order of the reservation
        reservation.setTotalReservation(this);
    }
}