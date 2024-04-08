package dat3.kino.screen;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This service class provides methods to perform operations related to screens.
 */
@Service
public class ScreenService {

    private final ScreenRepository screenRepository;

    /**
     * Constructs a new ScreenService with the given ScreenRepository.
     *
     * @param screenRepository The repository for accessing screen data.
     */
    public ScreenService(ScreenRepository screenRepository) {
        this.screenRepository = screenRepository;
    }

    /**
     * Retrieves all screens and converts them to DTOs.
     *
     * @return A list of ScreenDTOs representing all screens.
     */
    public List<ScreenDTO> getAllScreens() {
        List<Screen> screens = screenRepository.findAll();
        return screens.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a screen by its ID.
     *
     * @param id The ID of the screen to retrieve.
     * @return The ScreenDTO representing the screen with the specified ID.
     * @throws ResponseStatusException If the screen with the given ID is not found.
     */
    public ScreenDTO getScreenById(int id) {
        Screen screen = screenRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Screen not found"));
        return new ScreenDTO(screen, false);
    }

    /**
     * Converts a screen entity to a DTO.
     *
     * @param screen The screen entity to convert.
     * @return The corresponding ScreenDTO.
     */
    public ScreenDTO convertToDTO(Screen screen) {
        ScreenDTO dto = new ScreenDTO();
        dto.setId(screen.getId());
        dto.setName(screen.getName());
        dto.setRows(screen.getRows());
        dto.setCapacity(screen.getCapacity());
        return dto;
    }

    /**
     * Converts a list of screen entities to DTOs.
     *
     * @param screens The list of screen entities to convert.
     * @return A list of ScreenDTOs.
     */
    public List<ScreenDTO> convertToDTOs(List<Screen> screens) {
        return screens.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
