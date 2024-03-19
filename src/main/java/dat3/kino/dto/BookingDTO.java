package dat3.kino.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dat3.kino.entity.Booking;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDTO {

    private int id;
    private LocalDateTime created;
    private LocalDateTime edited;
    private double totalPrice;

    public BookingDTO(Booking t, boolean includeAll) {
        this.id = t.getId();
        this.totalPrice = t.getTotalPrice();
        if (includeAll) {
            this.created = t.getCreated();
            this.edited = t.getEdited();
        }
    }
}
