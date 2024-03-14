package dat3.kino.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ScreenDTO {

    private int id;
    private String name;
    private int capacity;
    private int rows;
    private LocalDateTime created;
    private LocalDateTime edited;

    public ScreenDTO(int id, String name, int capacity, int rows, LocalDateTime created, LocalDateTime edited) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.rows = rows;
        this.created = created;
        this.edited = edited;
    }
}