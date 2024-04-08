package dat3.kino.cinema;

import dat3.kino.screen.ScreenDTO;
import dat3.kino.screen.Screen;
import dat3.kino.screen.ScreenService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Service class for managing cinema-related operations.
 */
@Service
public class CinemaService {

    private final CinemaRepository cinemaRepository;
    private final ScreenService screenService;

    /**
     * Constructs a new CinemaService with the specified repositories.
     *
     * @param cinemaRepository The repository for cinema entities.
     * @param screenService    The service for screen-related operations.
     */
    public CinemaService(CinemaRepository cinemaRepository, ScreenService screenService) {
        this.cinemaRepository = cinemaRepository;
        this.screenService = screenService;
    }

    /**
     * Retrieves all cinemas.
     *
     * @return A list of CinemaDTO containing all cinemas.
     */
    public List<CinemaDTO> getAllCinemas() {
        List<Cinema> cinemas = cinemaRepository.findAll();
        return cinemas.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a cinema by its ID.
     *
     * @param id The ID of the cinema to retrieve.
     * @return The CinemaDTO representing the cinema with the specified ID.
     * @throws ResponseStatusException If the cinema with the specified ID is not found.
     */
    public CinemaDTO getCinemaById(int id) {
        Cinema cinema = cinemaRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Cinema not found"));

        return new CinemaDTO(cinema, false);
    }

    /**
     * Retrieves all screens associated with a cinema.
     *
     * @param cinemaId The ID of the cinema.
     * @return A list of ScreenDTO containing all screens associated with the specified cinema.
     * @throws RuntimeException If the cinema with the specified ID is not found.
     */
    public List<ScreenDTO> getScreensByCinemaId(int cinemaId) {
        Cinema cinema = cinemaRepository.findById(cinemaId).orElseThrow(() -> new RuntimeException("Cinema not found"));
        List<Screen> screens = cinema.getScreens();
        return screenService.convertToDTOs(screens);
    }

    // Helper method to convert Cinema entity to CinemaDTO
    private CinemaDTO convertToDto(Cinema cinema) {
        CinemaDTO cinemaDTO = new CinemaDTO();
        cinemaDTO.setId(cinema.getId());
        cinemaDTO.setName(cinema.getName());
        cinemaDTO.setLocation(cinema.getLocation());
        // You can set other properties as needed
        return cinemaDTO;
    }
}