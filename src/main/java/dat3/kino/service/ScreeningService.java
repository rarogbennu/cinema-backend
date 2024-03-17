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

@Service
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final CinemaRepository cinemaRepository;
    private final ScreenRepository screenRepository;

    public ScreeningService(ScreeningRepository screeningRepository,
                            MovieRepository movieRepository,
                            CinemaRepository cinemaRepository,
                            ScreenRepository screenRepository) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.cinemaRepository = cinemaRepository;
        this.screenRepository = screenRepository;
    }

    public List<ScreeningDTO> getAllScreenings() {
        List<Screening> screenings = screeningRepository.findAll();
        return screenings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ScreeningDTO getScreeningById(int id) {
        Screening screening = screeningRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Screening not found"));
        return new ScreeningDTO(screening, false);
    }

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


    public void deleteScreening(int id) {
        if (!screeningRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Screening not found");
        }
        screeningRepository.deleteById(id);
    }

    // Convert entity to DTO
    private ScreeningDTO convertToDTO(Screening screening) {
        ScreeningDTO dto = new ScreeningDTO();
        dto.setId(screening.getId());
        dto.setDate(screening.getDate());
        dto.setMovieId(screening.getMovie().getId());
        dto.setCinemaId(screening.getCinema().getId());
        dto.setScreenId(screening.getScreen().getId());
        return dto;
    }


    // Get List of DTOs
    private List<ScreeningDTO> convertToDTOs(List<Screening> screenings) {
        return screenings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
