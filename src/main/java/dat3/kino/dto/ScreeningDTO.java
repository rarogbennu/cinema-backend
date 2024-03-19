package dat3.kino.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dat3.kino.entity.Screening;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScreeningDTO {

    private int id;
    private LocalDateTime date;
    private int movieId;
    private int cinemaId;
    private int screenId;
    private boolean is3D; // Added field to include is3D information

    public ScreeningDTO(Screening screening, boolean includeAll) {
        this.id = screening.getId();
        this.date = screening.getDate();
        this.movieId = screening.getMovie().getId();
        this.cinemaId = screening.getCinema().getId();
        this.screenId = screening.getScreen().getId();
        this.is3D = screening.is3D();
    }
}
