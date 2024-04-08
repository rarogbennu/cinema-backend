package dat3.kino.screen;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScreenDTO {

    private int id;
    private String name;
    private int capacity;
    private int rows;
    private LocalDateTime created;
    private LocalDateTime edited;

    public ScreenDTO(Screen s, boolean includeAll) {
        this.id = s.getId();
        this.name = s.getName();
        this.capacity = s.getCapacity();
        this.rows = s.getRows();
        if (includeAll) {
            this.created = s.getCreated();
            this.edited = s.getEdited();
        }
    }
}