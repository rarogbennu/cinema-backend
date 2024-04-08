package dat3.kino.seat;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


/**
 * This service class provides methods to perform operations related to seats.
 */
@Service
public class SeatService {

    private final SeatRepository seatRepository;

    /**
     * Constructs a new SeatService with the given SeatRepository.
     *
     * @param seatRepository The repository for accessing seat data.
     */
    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    /**
     * Retrieves all seats and converts them to DTOs.
     *
     * @return A list of SeatDTOs representing all seats.
     */
    public List<SeatDTO> getAllSeats() {
        List<Seat> seats = seatRepository.findAll();
        return seats.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a seat by its ID.
     *
     * @param id The ID of the seat to retrieve.
     * @return The SeatDTO representing the seat with the specified ID.
     * @throws ResponseStatusException If the seat with the given ID is not found.
     */
    public SeatDTO getSeatById(int id) {
        Seat seat = seatRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat not found"));
        return new SeatDTO(seat);
    }

    /**
     * Retrieves all seats belonging to a screen.
     *
     * @param screenId The ID of the screen.
     * @return A list of SeatDTOs representing all seats belonging to the specified screen.
     * @throws ResponseStatusException If no seats are found for the provided screen ID.
     */
    public List<SeatDTO> getSeatsByScreenId(int screenId) {
        List<Seat> seats = seatRepository.findByScreenId(screenId);
        if (seats.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No seats found for the provided screen ID");
        }
        return seats.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts a seat entity to a DTO.
     *
     * @param seat The seat entity to convert.
     * @return The corresponding SeatDTO.
     */
    private SeatDTO convertToDTO(Seat seat) {
        return new SeatDTO(seat);
    }

}

