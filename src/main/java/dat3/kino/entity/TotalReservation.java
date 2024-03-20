package dat3.kino.entity;

import jakarta.persistence.*;
import lombok.Getter;
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

    // Add total price field
    private double totalPrice;

    public TotalReservation() {
        this.reservations = new ArrayList<>(); // Initialize the reservations list
        this.totalPrice = 0.0; // Initialize total price
    }

    public void addReservation(Reservation reservation) {
        // Add the reservation to the list of reservations
        reservations.add(reservation);
        // Set the order of the reservation
        reservation.setTotalReservation(this);
        // Update total price
        calculateTotalPrice();
    }

    public void calculateTotalPrice() {
        // Calculate total price by summing up the prices of all reservations
        this.totalPrice = reservations.stream()
                .mapToDouble(Reservation::getPrice)
                .sum();
    }
}