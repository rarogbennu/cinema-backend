package dat3.kino.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import dat3.kino.dto.MovieOmdbResponse;
import dat3.kino.entity.Movie;
import dat3.kino.service.MovieService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping()
    public List<MovieOmdbResponse> getAllMovies() { return movieService.getAllMovies();}

    @GetMapping("/{imdbId}")
    public Movie getMovieByImdbId(@PathVariable String imdbId) {
        return movieService.getMovieByImdbId(imdbId);
    }

    @RequestMapping("/api/imdbid/{imdbId}")
    public Movie getMovie(@PathVariable String imdbId){
        return movieService.getMovieByImdbId(imdbId);
    }

    @PostMapping("/api/{imdbId}")
    public Movie addMovie(@PathVariable String imdbId) throws JsonProcessingException {
        return movieService.addMovie(imdbId);
    }

    @DeleteMapping("api/{imdbId}")
    public void deleteMovieByImdbId(@PathVariable String imdbId) {
        movieService.deleteMovieByImdbId(imdbId);
    }


}
