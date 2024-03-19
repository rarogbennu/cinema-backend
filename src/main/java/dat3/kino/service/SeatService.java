package dat3.kino.service;

import dat3.kino.dto.SeatDTO;
import dat3.kino.entity.Seat;
import dat3.kino.repository.SeatRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<SeatDTO> getAllSeats() {
        List<Seat> seats = seatRepository.findAll();
        return seats.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public SeatDTO getSeatById(int id) {
        Seat seat = seatRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat not found"));
        return new SeatDTO(seat);
    }

    // Convert entity to DTO
    private SeatDTO convertToDTO(Seat seat) {
        return new SeatDTO(seat);
    }
}
