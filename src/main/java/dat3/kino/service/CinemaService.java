package dat3.kino.service;

import dat3.kino.dto.CinemaDTO;
import dat3.kino.entity.Cinema;
import dat3.kino.repository.CinemaRepository;
import dat3.kino.repository.ScreeningRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CinemaService {

    private final CinemaRepository cinemaRepository;

    public CinemaService(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    public List<CinemaDTO> getAllCinemas() {
        List<Cinema> cinemas = cinemaRepository.findAll();
        return cinemas.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
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
