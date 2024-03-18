package dat3.kino.service;

import dat3.kino.dto.TotalReservationDTO;
import dat3.kino.entity.Movie;
import dat3.kino.entity.Seat;
import dat3.kino.entity.Reservation;
import dat3.kino.entity.Screening;
import dat3.kino.entity.TotalReservation;
import dat3.kino.entity.PriceCategory;
import dat3.kino.repository.PriceCategoryRepository;
import dat3.kino.repository.TotalReservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TotalReservationService {

    private final TotalReservationRepository totalReservationRepository;
    private final PriceCategoryRepository priceCategoryRepository;

    public TotalReservationService(TotalReservationRepository totalReservationRepository, PriceCategoryRepository priceCategoryRepository) {
        this.totalReservationRepository = totalReservationRepository;
        this.priceCategoryRepository = priceCategoryRepository;
    }

    public List<TotalReservationDTO> getAllTotalReservations() {
        List<TotalReservation> totalReservations = totalReservationRepository.findAll();
        return totalReservations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TotalReservationDTO getTotalReservationById(int id) {
        TotalReservation totalReservation = totalReservationRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "TotalReservation not found"));
        return new TotalReservationDTO(totalReservation, false);
    }


    public TotalReservationDTO createTotalReservation(List<Reservation> reservations) {
        TotalReservation totalReservation = new TotalReservation();
        totalReservation.getReservations().addAll(reservations); // Reservation to TotalReservation
        TotalReservation savedTotalReservation = totalReservationRepository.save(totalReservation);
        return convertToDTO(savedTotalReservation);
    }



    public void deleteTotalReservation(int id) {
        if (!totalReservationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "TotalReservation not found");
        }
        totalReservationRepository.deleteById(id);
    }

    // Convert entity to DTO
    private TotalReservationDTO convertToDTO(TotalReservation totalReservation) {
        TotalReservationDTO dto = new TotalReservationDTO();
        dto.setId(totalReservation.getId());
        // Set other fields as needed
        return dto;
    }

    // Get List of DTOs
    private List<TotalReservationDTO> convertToDTOs(List<TotalReservation> totalReservations) {
        return totalReservations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public double calculateTotalPrice(Screening screening, Movie movie, List<Seat> selectedSeats) {
        double totalPrice = 0.0;

        // Fetch price categories from database
        List<PriceCategory> priceCategories = priceCategoryRepository.findAll();

        // Extract movie runtime without the " min" suffix
        int movieRuntime = extractMovieRuntime(movie.getRuntime());

        // Calculate the base price for each seat based on its price category
        for (Seat seat : selectedSeats) {
            PriceCategory priceCategory = seat.getPriceCategory();
            double seatPrice = priceCategory.getPrice();

            // Adjust price for 3D screenings
            if (screening.is3D()) {
                seatPrice += priceCategory.getAdditional3DCost();
            }

            // Adjust price for long movies
            if (movieRuntime > 150) {
                seatPrice += priceCategory.getAdditionalLongMovieCost();
            }

            totalPrice += seatPrice;
        }

        // Apply discounts based on order size
        int orderSize = selectedSeats.size();
        if (orderSize >= 6 && orderSize <= 10) {
            // No change in price
        } else if (orderSize >= 11) {
            totalPrice *= 0.95; // 5% discount for orders of 11 or more seats
        } else {
            totalPrice *= 1.05; // 5% additional charge for orders of 1-5 seats
        }

        return totalPrice;
    }

    private int extractMovieRuntime(String runtime) {
        // Remove " min" suffix and extract only the number
        String[] parts = runtime.split(" ");
        return Integer.parseInt(parts[0]);
    }

}
