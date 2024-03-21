package dat3.kino.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import dat3.kino.api_facade.OmdbFacade;
import dat3.kino.dto.MovieOmdbResponse;
import dat3.kino.entity.Cinema;
import dat3.kino.entity.Movie;
import dat3.kino.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This service class provides methods to perform operations related to movies.
 */
@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    private OmdbFacade omdbFacade;

    /**
     * Constructs a new MovieService with the specified repository.
     *
     * @param movieRepository The repository for accessing movie data.
     */
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Retrieves a movie by its ID.
     *
     * @param id The ID of the movie to retrieve.
     * @return The Movie entity with the specified ID.
     * @throws ResponseStatusException If the movie with the given ID is not found.
     */
    public Movie getMovieById(int id) {
        return movieRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));
    }

    /**
     * Retrieves a movie by its IMDb ID.
     *
     * @param imdbId The IMDb ID of the movie to retrieve.
     * @return The Movie entity with the specified IMDb ID.
     * @throws ResponseStatusException If the movie with the given IMDb ID is not found.
     */
    public Movie getMovieByImdbId(String imdbId) {
        return movieRepository.findByImdbID(imdbId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));
    }

    /**
     * Adds a movie to the database using its IMDb ID.
     *
     * @param imdbId The IMDb ID of the movie to add.
     * @return The added Movie entity.
     * @throws JsonProcessingException   If there is an issue processing JSON data.
     * @throws ResponseStatusException   If the movie already exists or cannot be added.
     */
    public Movie addMovie(String imdbId) throws JsonProcessingException {
        MovieOmdbResponse dto = omdbFacade.getMovie(imdbId);

        Movie movie = Movie.builder()
                .title(dto.getTitle())
                .year(dto.getYear())
                .rated(dto.getRated())
                .released(dto.getReleased())
                .runtime(dto.getRuntime())
                .genre(dto.getGenre())
                .director(dto.getDirector())
                .writer(dto.getWriter())
                .actors(dto.getActors())
                .metascore(dto.getMetascore())
                .imdbRating(dto.getImdbRating())
                .imdbVotes(dto.getImdbVotes())
                .website(dto.getWebsite())
                .response(dto.getResponse())
                .plot(dto.getPlot())
                .poster(dto.getPoster())
                .imdbID(dto.getImdbID())
                .build();
        try {
            movieRepository.save(movie);
            return movie;
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getRootCause().getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not add movie");
        }
    }

    /**
     * Retrieves all movies and converts them to DTOs.
     *
     * @return A list of MovieOmdbResponse DTOs representing all movies.
     */
    public List<MovieOmdbResponse> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * Deletes a movie by its IMDb ID.
     *
     * @param imdbId The IMDb ID of the movie to delete.
     * @throws ResponseStatusException If the movie with the given IMDb ID is not found.
     */
    public void deleteMovieByImdbId(String imdbId) {
        movieRepository.findByImdbID(imdbId).ifPresentOrElse(
                movieRepository::delete,
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");
                }
        );
    }

    /**
     * Converts a movie entity to a DTO.
     *
     * @param movie The movie entity to convert.
     * @return The corresponding MovieOmdbResponse DTO.
     */
    public MovieOmdbResponse convertToDto(Movie movie) {
        MovieOmdbResponse movieDto = new MovieOmdbResponse();
        movieDto.setId(movie.getId());
        movieDto.setTitle(movie.getTitle());
        movieDto.setYear(movie.getYear());
        movieDto.setRated(movie.getRated());
        movieDto.setReleased(movie.getReleased());
        movieDto.setRuntime(movie.getRuntime());
        movieDto.setGenre(movie.getGenre());
        movieDto.setDirector(movie.getDirector());
        movieDto.setWriter(movie.getWriter());
        movieDto.setActors(movie.getActors());
        movieDto.setPlot(movie.getPlot());
        movieDto.setPoster(movie.getPoster());
        movieDto.setMetascore(movie.getMetascore());
        movieDto.setImdbRating(movie.getImdbRating());
        movieDto.setImdbVotes(movie.getImdbVotes());
        movieDto.setImdbID(movie.getImdbID());
        movieDto.setWebsite(movie.getWebsite());
        movieDto.setResponse(movie.getResponse());

        return movieDto;
    }
}

