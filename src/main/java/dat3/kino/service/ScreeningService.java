package dat3.kino.service;

import dat3.kino.dto.ScreeningDTO;
import dat3.kino.entity.Screening;
import dat3.kino.repository.CinemaRepository;
import dat3.kino.repository.MovieRepository;
import dat3.kino.repository.ScreenRepository;
import dat3.kino.repository.ScreeningRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This service class provides methods to perform operations related to screenings.
 */
@Service
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final CinemaRepository cinemaRepository;
    private final ScreenRepository screenRepository;

    /**
     * Constructs a new ScreeningService with the specified repositories.
     *
     * @param screeningRepository The repository for accessing screening data.
     * @param movieRepository     The repository for accessing movie data.
     * @param cinemaRepository    The repository for accessing cinema data.
     * @param screenRepository    The repository for accessing screen data.
     */
    public ScreeningService(ScreeningRepository screeningRepository,
                            MovieRepository movieRepository,
                            CinemaRepository cinemaRepository,
                            ScreenRepository screenRepository) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.cinemaRepository = cinemaRepository;
        this.screenRepository = screenRepository;
    }

    /**
     * Retrieves all screenings and converts them to DTOs.
     *
     * @return A list of ScreeningDTOs representing all screenings.
     */
    public List<ScreeningDTO> getAllScreenings() {
        List<Screening> screenings = screeningRepository.findAll();
        return screenings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a screening by its ID.
     *
     * @param id The ID of the screening to retrieve.
     * @return The ScreeningDTO representing the screening with the specified ID.
     * @throws ResponseStatusException If the screening with the given ID is not found.
     */
    public ScreeningDTO getScreeningById(int id) {
        Screening screening = screeningRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Screening not found"));
        return new ScreeningDTO(screening, false);
    }

    /**
     * Retrieves all screenings associated with a cinema.
     *
     * @param cinemaId The ID of the cinema.
     * @return A list of ScreeningDTOs representing all screenings associated with the specified cinema.
     */
    public List<ScreeningDTO> getScreeningsByCinemaId(int cinemaId) {
        List<Screening> screenings = screeningRepository.findByCinemaId(cinemaId);
        return screenings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new screening.
     *
     * @param screeningDTO The DTO containing information about the screening to be created.
     * @return The ScreeningDTO representing the created screening.
     * @throws ResponseStatusException If the associated movie, cinema, or screen is not found.
     */
    public ScreeningDTO createScreening(ScreeningDTO screeningDTO) {
        Screening screening = new Screening();
        screening.setDate(screeningDTO.getDate());
        screening.setMovie(movieRepository.findById(screeningDTO.getMovieId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found")));
        screening.setCinema(cinemaRepository.findById(screeningDTO.getCinemaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cinema not found")));
        screening.setScreen(screenRepository.findById(screeningDTO.getScreenId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Screen not found")));

        Screening savedScreening = screeningRepository.save(screening);
        return convertToDTO(savedScreening);
    }

    /**
     * Deletes a screening by its ID.
     *
     * @param id The ID of the screening to delete.
     * @throws ResponseStatusException If the screening with the given ID is not found.
     */
    public void deleteScreening(int id) {
        if (!screeningRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Screening not found");
        }
        screeningRepository.deleteById(id);
    }

    /**
     * Converts a screening entity to a DTO.
     *
     * @param screening The screening entity to convert.
     * @return The corresponding ScreeningDTO.
     */
    private ScreeningDTO convertToDTO(Screening screening) {
        ScreeningDTO dto = new ScreeningDTO();
        dto.setId(screening.getId());
        dto.setDate(screening.getDate());
        dto.setMovieId(screening.getMovie().getId());
        dto.setCinemaId(screening.getCinema().getId());
        dto.setScreenId(screening.getScreen().getId());
        return dto;
    }

    /**
     * Converts a list of screening entities to DTOs.
     *
     * @param screenings The list of screening entities to convert.
     * @return A list of ScreeningDTOs representing the screenings.
     */
    private List<ScreeningDTO> convertToDTOs(List<Screening> screenings) {
        return screenings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}

