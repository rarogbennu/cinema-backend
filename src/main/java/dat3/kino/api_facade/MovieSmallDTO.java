package dat3.kino.api_facade;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieSmallDTO {

    private String id;
    private String imdbID;
    private String title;
    private String runtime;
    private String year;
    private String poster;
}
