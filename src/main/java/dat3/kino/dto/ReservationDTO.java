package dat3.kino.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dat3.kino.entity.Reservation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationDTO {

    private int id;
    private int screeningId;
    private int seatId;
    private int totalReservationId;
    private String dummyUser;
    private LocalDateTime created;
    private LocalDateTime edited;

    // Constructor to accept screeningId, seatId, and dummyUser
    public ReservationDTO(int screeningId, int seatId, String dummyUser) {
        this.screeningId = screeningId;
        this.seatId = seatId;
        this.dummyUser = dummyUser;
    }

    public ReservationDTO(Reservation r, boolean includeAll) {
        this.id = r.getId();
        this.screeningId = r.getScreening().getId();
        this.seatId = r.getSeat().getId();
        this.totalReservationId = r.getTotalReservation().getId();
        this.dummyUser = r.getDummyUser();
        if (includeAll) {
            this.created = r.getCreated();
            this.edited = r.getEdited();
        }
    }
}

