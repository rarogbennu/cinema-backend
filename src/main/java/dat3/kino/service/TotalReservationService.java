package dat3.kino.service;

import dat3.kino.entity.Movie;
import dat3.kino.entity.Seat;
import dat3.kino.entity.PriceCategory;
import dat3.kino.entity.Screening;
import dat3.kino.repository.PriceCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TotalReservationService {

    private final PriceCategoryRepository priceCategoryRepository;

    public TotalReservationService(PriceCategoryRepository priceCategoryRepository) {
        this.priceCategoryRepository = priceCategoryRepository;
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