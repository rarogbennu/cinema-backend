package dat3.kino.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dat3.kino.entity.Seat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SeatDTO {

    private int id;
    private int screenId;
    private LocalDateTime created;
    private LocalDateTime edited;

    public SeatDTO(Seat seat, boolean includeAll) {
        this.id = seat.getId();
        this.screenId = seat.getScreen().getId();

    }
}
