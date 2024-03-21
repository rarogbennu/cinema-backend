package dat3.kino.service;

import dat3.kino.dto.ReservationDTO;
import dat3.kino.dto.ScreeningDTO;
import dat3.kino.dto.TotalReservationDTO;
import dat3.kino.entity.Reservation;
import dat3.kino.entity.Screening;
import dat3.kino.entity.Seat;
import dat3.kino.entity.TotalReservation;
import dat3.kino.entity.PriceCategory;
import dat3.kino.repository.ReservationRepository;
import dat3.kino.repository.ScreeningRepository;
import dat3.kino.repository.SeatRepository;
import dat3.kino.repository.TotalReservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This service class provides methods related to reservations.
 */
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final TotalReservationRepository totalReservationRepository;

    /**
     * Constructs a new ReservationService with the specified repositories.
     *
     * @param reservationRepository    The repository for accessing reservation data.
     * @param screeningRepository     The repository for accessing screening data.
     * @param seatRepository          The repository for accessing seat data.
     * @param totalReservationRepository  The repository for accessing total reservation data.
     */
    public ReservationService(ReservationRepository reservationRepository,
                              ScreeningRepository screeningRepository,
                              SeatRepository seatRepository, TotalReservationRepository totalReservationRepository) {
        this.reservationRepository = reservationRepository;
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
        this.totalReservationRepository = totalReservationRepository;

    }

    /**
     * Retrieves all reservations.
     *
     * @return A list of all reservations.
     */
    public List<ReservationDTO> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return convertToDTOs(reservations);
    }

    /**
     * Retrieves a reservation by its ID.
     *
     * @param id The ID of the reservation to retrieve.
     * @return The reservation with the specified ID.
     * @throws ResponseStatusException If the reservation is not found.
     */
    public ReservationDTO getReservationById(int id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));
        return convertToDTO(reservation);
    }


//    public ReservationDTO createReservation(ReservationDTO reservationDTO) {
//        // Find screening and seat
//        Screening screening = screeningRepository.findById(reservationDTO.getScreeningId())
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Screening not found"));
//        Seat seat = seatRepository.findById(reservationDTO.getSeatId())
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat not found"));
//
//        // Calculate reservation price
//        double reservationPrice = calculateReservationPrice(screening, seat);
//
//        // Create a new total reservation if it doesn't exist
//        TotalReservation totalReservation = new TotalReservation();
//        totalReservationRepository.save(totalReservation);
//
//        // Create the reservation
//        Reservation reservation = new Reservation();
//        reservation.setScreening(screening);
//        reservation.setSeat(seat);
//        reservation.setDummyUser(reservationDTO.getDummyUser());
//        reservation.setTotalReservation(totalReservation);
//        reservation.setPrice(reservationPrice);
//
//        // Save reservation
//        Reservation savedReservation = reservationRepository.save(reservation);
//
//        // Update total price in the associated TotalReservation
//        totalReservation.setTotalPrice(totalReservation.getTotalPrice() + reservationPrice);
//        totalReservationRepository.save(totalReservation);
//
//        // Convert saved reservation to DTO and return it
//        return convertToDTO(savedReservation);
//    }

    /**
     * Creates a new total reservation based on the provided list of reservation DTOs.
     *
     * @param reservationDTOs The list of reservation DTOs representing the reservations to create.
     * @return The DTO representing the total reservation containing the created reservations.
     */
    public TotalReservationDTO createReservation(List<ReservationDTO> reservationDTOs) {
        // Create a new total reservation
        TotalReservation totalReservation = new TotalReservation();
        totalReservationRepository.save(totalReservation);

        // List to hold saved reservations
        List<Reservation> savedReservations = new ArrayList<>();

        // Loop through reservationDTOs and create/save reservations
        for (ReservationDTO reservationDTO : reservationDTOs) {
            // Find screening and seat
            Screening screening = screeningRepository.findById(reservationDTO.getScreeningId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Screening not found"));
            Seat seat = seatRepository.findById(reservationDTO.getSeatId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat not found"));

            // Calculate reservation price
            double reservationPrice = calculateReservationPrice(screening, seat);

            // Create reservation
            Reservation reservation = new Reservation();
            reservation.setScreening(screening);
            reservation.setSeat(seat);
            reservation.setDummyUser(reservationDTO.getDummyUser());
            reservation.setTotalReservation(totalReservation);
            reservation.setPrice(reservationPrice);

            // Save reservation
            Reservation savedReservation = reservationRepository.save(reservation);
            savedReservations.add(savedReservation);
        }

        // Update total price in the associated TotalReservation
        double totalPrice = savedReservations.stream().mapToDouble(Reservation::getPrice).sum();
        totalReservation.setTotalPrice(totalPrice);
        totalReservationRepository.save(totalReservation);

        // Convert and return the saved total reservation DTO
        return convertTotalReservationToDTO(totalReservation);
    }

    /**
     * Deletes a reservation by its ID.
     *
     * @param id The ID of the reservation to delete.
     * @throws ResponseStatusException If the reservation is not found.
     */
    public void deleteReservation(int id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found");
        }
        reservationRepository.deleteById(id);
    }

    /**
     * Converts a Reservation entity to its corresponding DTO.
     *
     * @param reservation The reservation entity to convert.
     * @return The DTO representing the reservation.
     */
    private ReservationDTO convertToDTO(Reservation reservation) {
        return new ReservationDTO(reservation, true);
    }

    /**
     * Converts a list of Reservation entities to a list of ReservationDTOs.
     *
     * @param reservations The list of reservation entities to convert.
     * @return The list of DTOs representing the reservations.
     */
    private List<ReservationDTO> convertToDTOs(List<Reservation> reservations) {
        return reservations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts a TotalReservation entity to its corresponding DTO.
     *
     * @param totalReservation The total reservation entity to convert.
     * @return The DTO representing the total reservation.
     */
    private TotalReservationDTO convertTotalReservationToDTO(TotalReservation totalReservation) {
        TotalReservationDTO totalReservationDTO = new TotalReservationDTO();
        totalReservationDTO.setId(totalReservation.getId());
        totalReservationDTO.setTotalPrice(totalReservation.getTotalPrice());
        // Additional fields can be added as needed
        return totalReservationDTO;
    }

    /**
     * Calculates the price of a reservation based on the given screening and seat.
     *
     * @param screening The screening associated with the reservation.
     * @param seat      The seat reserved.
     * @return The calculated price of the reservation.
     */
    public double calculateReservationPrice(Screening screening, Seat seat) {
        // Fetch the associated price category for the seat
        PriceCategory priceCategory = seat.getPriceCategory();

        // Calculate the base price for the seat based on its price category
        double basePrice = priceCategory.getPrice();

        // Adjust price for 3D screenings
        if (screening.is3D()) {
            basePrice += priceCategory.getAdditional3DCost();
        }

        // Adjust price for long movies
        if (isLongMovie(screening.getMovie().getRuntime())) {
            basePrice += priceCategory.getAdditionalLongMovieCost();
        }

        return basePrice;
    }

    /**
     * Checks if a movie is considered long based on its runtime.
     *
     * @param runtime The runtime of the movie.
     * @return True if the movie is considered long, false otherwise.
     */
    private boolean isLongMovie(String runtime) {
        int movieRuntime = extractMovieRuntime(runtime);
        return movieRuntime > 150;
    }


    /**
     * Extracts the movie runtime without the " min" suffix.
     *
     * @param runtime The runtime string of the movie.
     * @return The integer value representing the movie runtime.
     */
    private int extractMovieRuntime(String runtime) {
        String[] parts = runtime.split(" ");
        return Integer.parseInt(parts[0]);
    }

    /**
     * Retrieves reservations for a specific screening.
     *
     * @param screeningId The ID of the screening.
     * @return A list of reservation DTOs for the specified screening.
     */
    public List<ReservationDTO> getReservationsByScreeningId(int screeningId) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Screening not found"));
        List<Reservation> reservations = reservationRepository.findByScreeningId(screeningId);
        return convertToDTOs(reservations);
    }
}

