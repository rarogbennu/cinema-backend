package dat3.kino.service;

import dat3.kino.dto.CinemaDTO;
import dat3.kino.dto.ScreenDTO;
import dat3.kino.entity.Cinema;
import dat3.kino.entity.Screen;
import dat3.kino.repository.CinemaRepository;
import dat3.kino.repository.ScreeningRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CinemaService {

    private final CinemaRepository cinemaRepository;
    private final ScreenService screenService;


    public CinemaService(CinemaRepository cinemaRepository, ScreenService screenService) {
        this.cinemaRepository = cinemaRepository;
        this.screenService = screenService;
    }

    public List<CinemaDTO> getAllCinemas() {
        List<Cinema> cinemas = cinemaRepository.findAll();
        return cinemas.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CinemaDTO getCinemaById(int id) {
        Cinema cinema = cinemaRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Cinema not found"));

        return new CinemaDTO(cinema, false);
    }

    public List<ScreenDTO> getScreensByCinemaId(int cinemaId) {
        Cinema cinema = cinemaRepository.findById(cinemaId).orElseThrow(() -> new RuntimeException("Cinema not found"));
        List<Screen> screens = cinema.getScreens();
        return screenService.convertToDTOs(screens);
    }

    private CinemaDTO convertToDto(Cinema cinema) {
        CinemaDTO cinemaDTO = new CinemaDTO();
        cinemaDTO.setId(cinema.getId());
        cinemaDTO.setName(cinema.getName());
        cinemaDTO.setLocation(cinema.getLocation());
        // You can set other properties as needed
        return cinemaDTO;
    }
}
