package dat3.kino.configuration;

import dat3.kino.cinema.Cinema;
import dat3.kino.screen.Screen;
import dat3.kino.seat.Seat;
import dat3.kino.pricing.priceCategory.PriceCategory;
import dat3.kino.cinema.CinemaRepository;
import dat3.kino.screen.ScreenRepository;
import dat3.kino.seat.SeatRepository;
import dat3.kino.pricing.priceCategory.PriceCategoryRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InitDataCinema implements ApplicationRunner {

    private final SeatRepository seatRepository;
    private final ScreenRepository screenRepository;
    private final CinemaRepository cinemaRepository;
    private final PriceCategoryRepository priceCategoryRepository;

    public InitDataCinema(SeatRepository seatRepository, ScreenRepository screenRepository, CinemaRepository cinemaRepository, PriceCategoryRepository priceCategoryRepository) {
        this.seatRepository = seatRepository;
        this.screenRepository = screenRepository;
        this.cinemaRepository = cinemaRepository;
        this.priceCategoryRepository = priceCategoryRepository;
    }

@Override
    public void run(ApplicationArguments args) {
        initScreens();
        initCinemas();
        initPriceCategories();
        initSeats();

    }

    public void initScreens() {
        List<Screen> screens = List.of(
                new Screen("Screen 1", 240, 20),
                new Screen("Screen 2", 240, 20),
                new Screen("Screen 3", 320, 22),
                new Screen("Screen 4", 320, 22),
                new Screen("Screen 5", 400, 25),
                new Screen("Screen 6", 400, 25)
        );

        screenRepository.saveAll(screens);
    }

    public void initCinemas() {
        List<Screen> screens = screenRepository.findAll();

        Cinema cinema1 = new Cinema("Cinema Copenhagen", "Copenhagen");
        cinema1.setScreens(List.of(screens.get(0), screens.get(1)));

        Cinema cinema2 = new Cinema("Cinema Roskilde", "Roskilde");
        cinema2.setScreens(List.of(screens.get(2), screens.get(3), screens.get(4), screens.get(5)));

        cinemaRepository.saveAll(List.of(cinema1, cinema2));
    }


    public void initPriceCategories() {
        List<PriceCategory> priceCategories = List.of(
                new PriceCategory("Economy", 80.00, 20.00, 20.00),
                new PriceCategory("Standard", 100.00, 20.00, 20.00),
                new PriceCategory("Premium", 150.00, 25.00, 25.00)
        );

        priceCategoryRepository.saveAll(priceCategories);
    }

    public void initSeats() {
        List<Screen> screens = screenRepository.findAll();
        List<PriceCategory> priceCategories = priceCategoryRepository.findAll();

        PriceCategory economyCategory = priceCategories.get(0);
        PriceCategory standardCategory = priceCategories.get(1);
        PriceCategory premiumCategory = priceCategories.get(2);

        for (Screen screen : screens) {
            int capacity = screen.getCapacity();
            int numRows = screen.getRows();

            for (int rowNumber = 1; rowNumber <= numRows; rowNumber++) {
                PriceCategory priceCategory;

                // Assign price categories based on row numbers
                if (rowNumber <= 2) {
                    priceCategory = economyCategory; // First two rows
                } else if (rowNumber > numRows - 3) {
                    priceCategory = premiumCategory; // Last three rows
                } else {
                    priceCategory = standardCategory; // Rest of the rows
                }

                for (int seatNumber = 1; seatNumber <= capacity / numRows; seatNumber++) {
                    Seat seat = new Seat();
                    seat.setScreen(screen);
                    seat.setId((screen.getId() - 1) * capacity + (rowNumber - 1) * (capacity / numRows) + seatNumber); // Calculate a unique seat ID
                    seat.setPriceCategory(priceCategory); // Assign the calculated price category to the seat

                    seatRepository.save(seat);
                    screen.getSeats().add(seat);
                }
            }

            screenRepository.save(screen);
        }
    }}


