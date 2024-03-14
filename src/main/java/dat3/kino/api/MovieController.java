package dat3.kino.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import dat3.kino.entity.Movie;
import dat3.kino.service.MovieService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/movies")
public class MovieController {

    MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @RequestMapping("/imdbid/{imdbId}")
    public Movie getMovie(@PathVariable String imdbId){
        return movieService.getMovieByImdbId(imdbId);
    }

    @PostMapping("/{imdbId}")
    public Movie addMovie(@PathVariable String imdbId) throws JsonProcessingException {
        return movieService.addMovie(imdbId);
    }
}
