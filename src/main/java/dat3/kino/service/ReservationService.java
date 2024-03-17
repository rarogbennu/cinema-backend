package dat3.kino.service;

import dat3.kino.dto.ReservationDTO;
import dat3.kino.entity.Reservation;
import dat3.kino.entity.Screening;
import dat3.kino.entity.Seat;
import dat3.kino.entity.TotalReservation;
import dat3.kino.repository.ReservationRepository;
import dat3.kino.repository.ScreeningRepository;
import dat3.kino.repository.SeatRepository;
import dat3.kino.repository.TotalReservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final TotalReservationRepository totalReservationRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ScreeningRepository screeningRepository,
                              SeatRepository seatRepository, TotalReservationRepository totalReservationRepository) {
        this.reservationRepository = reservationRepository;
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
        this.totalReservationRepository = totalReservationRepository;

    }

    public List<ReservationDTO> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return convertToDTOs(reservations);
    }

    public ReservationDTO getReservationById(int id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));
        return convertToDTO(reservation);
    }

    public ReservationDTO createReservation(ReservationDTO reservationDTO) {
        Reservation reservation = new Reservation();

        // Find screening and seat
        Screening screening = screeningRepository.findById(reservationDTO.getScreeningId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Screening not found"));
        Seat seat = seatRepository.findById(reservationDTO.getSeatId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat not found"));

        // Create a new total reservation
        TotalReservation totalReservation = new TotalReservation();
        totalReservationRepository.save(totalReservation);

        // Set reservation attributes
        reservation.setScreening(screening);
        reservation.setSeat(seat);
        reservation.setDummyUser(reservationDTO.getDummyUser());
        reservation.setTotalReservation(totalReservation);

        // Save reservation
        Reservation savedReservation = reservationRepository.save(reservation);
        return convertToDTO(savedReservation);
    }

    public void deleteReservation(int id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found");
        }
        reservationRepository.deleteById(id);
    }

    // Convert entity to DTO
    private ReservationDTO convertToDTO(Reservation reservation) {
        return new ReservationDTO(reservation, true);
    }

    // Get List of DTOs
    private List<ReservationDTO> convertToDTOs(List<Reservation> reservations) {
        return reservations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}

