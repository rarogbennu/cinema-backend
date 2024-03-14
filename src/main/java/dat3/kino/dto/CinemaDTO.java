package dat3.kino.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CinemaDTO {

    private int id;
    private String name;
    private String location;
    private List<ScreenDTO> screens;
    private LocalDateTime created;
    private LocalDateTime edited;

    public CinemaDTO(int id, String name, String location, List<ScreenDTO> screens, LocalDateTime created, LocalDateTime edited) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.screens = screens;
        this.created = created;
        this.edited = edited;
    }
}