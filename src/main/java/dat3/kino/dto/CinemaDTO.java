package dat3.kino.dto;

import dat3.kino.entity.Cinema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public CinemaDTO(Cinema c, boolean includeAll) {
        this.id = c.getId();
        this.name = c.getName();
        this.location = c.getLocation();
        if (includeAll) {
            this.created = c.getCreated();
            this.edited = c.getEdited();
        }
        // Convert List<Screen> to List<ScreenDTO>
        this.screens = c.getScreens().stream()
                .map(screen -> new ScreenDTO(screen, false))
                .collect(Collectors.toList());
    }
}