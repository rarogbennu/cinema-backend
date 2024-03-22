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

    public TotalReservationDTO createReservation(List<ReservationDTO> reservationDTOs) {
        // Create a new total reservation
        TotalReservation totalReservation = new TotalReservation();
        totalReservationRepository.save(totalReservation);

        // List to hold saved reservations
        List<Reservation> savedReservations = new ArrayList<>();

        // Loop through reservationDTOs and create/save reservations
        for (ReservationDTO reservationDTO : reservationDTOs) {
            // Find screening og seat
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


        // Apply discounts based on the total number of reservations
        int orderSize = savedReservations.size();
        if (orderSize >= 6 && orderSize <= 10) {
            // No change in price
        } else if (orderSize >= 11) {
            totalPrice *= 0.95; // 5% discount for orders of 11 or more seats
        } else {
            totalPrice *= 1.05; // 5% additional charge for orders of 1-5 seats
        }

        totalReservation.setTotalPrice(totalPrice);
        totalReservationRepository.save(totalReservation);

        // Convert and return the saved total reservation
        return convertTotalReservationToDTO(totalReservation);
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

    // Metode til at konvertere TotalReservation til TotalReservationDTO
    private TotalReservationDTO convertTotalReservationToDTO(TotalReservation totalReservation) {
        TotalReservationDTO totalReservationDTO = new TotalReservationDTO();
        totalReservationDTO.setId(totalReservation.getId());
        totalReservationDTO.setTotalPrice(totalReservation.getTotalPrice());
        // Andre felter kan tilføjes efter behov
        return totalReservationDTO;
    }


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

    // Method to check if a movie is considered long based on its runtime
    private boolean isLongMovie(String runtime) {
        int movieRuntime = extractMovieRuntime(runtime);
        return movieRuntime > 150;
    }

    // Method to extract movie runtime without the " min" suffix
    private int extractMovieRuntime(String runtime) {
        String[] parts = runtime.split(" ");
        return Integer.parseInt(parts[0]);
    }

    public List<ReservationDTO> getReservationsByScreeningId(int screeningId) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Screening not found"));
        List<Reservation> reservations = reservationRepository.findByScreeningId(screeningId);
        return convertToDTOs(reservations);
    }
}

