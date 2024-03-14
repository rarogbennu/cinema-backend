package dat3.kino.service;

import dat3.kino.dto.CinemaDTO;
import dat3.kino.dto.ScreenDTO;
import dat3.kino.entity.Cinema;
import dat3.kino.entity.Screen;
import dat3.kino.repository.ScreenRepository;
import dat3.kino.repository.ScreeningRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScreenService {
    private final ScreenRepository screenRepository;

    public ScreenService(ScreenRepository screenRepository) {
        this.screenRepository = screenRepository;
    }

    public List<ScreenDTO> getAllScreens() {
        List<Screen> screens = screenRepository.findAll();
        return screens.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ScreenDTO getScreenById(int id) {
        Screen screen = screenRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Screen not found"));
        return new ScreenDTO(screen, false);
    }

    // Convert entity to DTO
    public ScreenDTO convertToDTO(Screen screen) {
        ScreenDTO dto = new ScreenDTO();
        dto.setId(screen.getId());
        dto.setName(screen.getName());
        dto.setRows(screen.getRows());
        dto.setCapacity(screen.getCapacity());
        return dto;
    }

    // Get List of DTOs
    public List<ScreenDTO> convertToDTOs(List<Screen> screens) {
        return screens.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}