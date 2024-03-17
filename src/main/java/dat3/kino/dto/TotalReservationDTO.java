package dat3.kino.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dat3.kino.entity.TotalReservation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TotalReservationDTO {

    private int id;
    private LocalDateTime created;
    private LocalDateTime edited;

    public TotalReservationDTO(TotalReservation t, boolean includeAll) {
        this.id = t.getId();
        if (includeAll) {
            this.created = t.getCreated();
            this.edited = t.getEdited();
        }
    }
}

