package dat3.kino.seat;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SeatDTO {

    private int id;
    private int screenId;
    private int priceCategoryId;

    public SeatDTO(Seat seat) {
        this.id = seat.getId();
        this.screenId = seat.getScreen().getId();
        this.priceCategoryId = seat.getPriceCategory().getId();
    }
}
