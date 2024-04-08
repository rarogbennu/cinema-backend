package dat3.kino.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import dat3.kino.cinema.Cinema;
import dat3.kino.cinema.CinemaRepository;
import dat3.kino.reservation.ReservationDTO;
import dat3.kino.totalReservation.TotalReservationDTO;
import dat3.kino.movie.Movie;
import dat3.kino.movie.MovieRepository;
import dat3.kino.movie.MovieService;
import dat3.kino.reservation.ReservationRepository;
import dat3.kino.reservation.ReservationService;
import dat3.kino.screen.Screen;
import dat3.kino.screen.ScreenRepository;
import dat3.kino.screening.Screening;
import dat3.kino.screening.ScreeningRepository;
import dat3.kino.seat.SeatRepository;
import dat3.kino.totalReservation.TotalReservationRepository;
import dat3.kino.totalReservation.TotalReservationService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class InitDataReservation implements ApplicationRunner {
    private final CinemaRepository cinemaRepository;
    private final ScreenRepository screenRepository;
    private final ScreeningRepository screeningRepository;
    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;
    private final TotalReservationRepository totalReservationRepository;
    private final MovieService movieService;
    private final MovieRepository movieRepository;
    private final TotalReservationService totalReservationService;
    private final ReservationService reservationService;



    public InitDataReservation(CinemaRepository cinemaRepository,
                               ScreenRepository screenRepository,
                               ScreeningRepository screeningRepository,
                               ReservationRepository reservationRepository,
                               SeatRepository seatRepository,
                               TotalReservationRepository totalReservationRepository,
                               MovieService movieService,
                               MovieRepository movieRepository,
                               TotalReservationService totalReservationService,
                               ReservationService reservationService) {
        this.cinemaRepository = cinemaRepository;
        this.screenRepository = screenRepository;
        this.screeningRepository = screeningRepository;
        this.reservationRepository = reservationRepository;
        this.seatRepository = seatRepository;
        this.totalReservationRepository = totalReservationRepository;
        this.movieService = movieService;
        this.movieRepository = movieRepository;
        this.totalReservationService = totalReservationService;
        this.reservationService = reservationService;
    }

    @Override
    public void run(ApplicationArguments args) throws JsonProcessingException {
        initMovies();
        initScreenings();
        initReservations();
//        initTotalReservation();
//        addReservationsToTotalReservationAndUpdate();
    }

    public void initMovies() throws JsonProcessingException {
        String[] imdbIds = {"tt0242423", "tt0499549", "tt0050976"};

        for (String imdbId : imdbIds) {
            movieService.addMovie(imdbId);
        }

        System.out.println("Movies added successfully.");
    }

    public void initScreenings() {
        // Find movies by ID
        Movie movie1 = movieRepository.findById(1).orElseThrow(() -> new RuntimeException("Movie with ID 1 not found"));
        Movie movie2 = movieRepository.findById(2).orElseThrow(() -> new RuntimeException("Movie with ID 2 not found"));
        Movie movie3 = movieRepository.findById(3).orElseThrow(() -> new RuntimeException("Movie with ID 3 not found"));

        // Find cinemas and screens by name (assuming you have methods to find them by name)
        Cinema cinemaCopenhagen = cinemaRepository.findByName("Cinema Copenhagen").orElseThrow(() -> new RuntimeException("Cinema Copenhagen not found"));
        Cinema cinemaRoskilde = cinemaRepository.findByName("Cinema Roskilde").orElseThrow(() -> new RuntimeException("Cinema Roskilde not found"));
        Screen screen1 = screenRepository.findByName("Screen 1").orElseThrow(() -> new RuntimeException("Screen 1 not found"));
        Screen screen2 = screenRepository.findByName("Screen 2").orElseThrow(() -> new RuntimeException("Screen 2 not found"));
        Screen screen4 = screenRepository.findByName("Screen 4").orElseThrow(() -> new RuntimeException("Screen 4 not found"));

        LocalDateTime now = LocalDateTime.now();

        // Create screenings using the found movies, cinemas, and screens
        Screening screening1 = new Screening(now, movie1, cinemaCopenhagen, screen1);
        Screening screening2 = new Screening(now.plusDays(1), movie2, cinemaCopenhagen, screen2);
        Screening screening3 = new Screening(now.plusDays(2), movie3, cinemaRoskilde, screen4);

        // Set some of the screenings as 3D
        screening1.set3D(true);
        screening2.set3D(false);
        screening3.set3D(true);

        // Save the screenings
        screeningRepository.saveAll(List.of(screening1, screening2, screening3));
        System.out.println("Screenings updated successfully.");
    }

    public void initReservations() {
        // Define the reservation data as provided in the POST request
        List<ReservationDTO> reservationDTOs = List.of(
                new ReservationDTO(1, 1, "TestUser1"),
                new ReservationDTO(1, 2, "TestUser1"),
                new ReservationDTO(1, 3, "TestUser1")
        );
        List<ReservationDTO> reservationDTOs2 = List.of(
                new ReservationDTO(2, 250, "TestUser1"),
                new ReservationDTO(2, 251, "TestUser1"),
                new ReservationDTO(2, 252, "TestUser1")
        );

        // Call the createReservation method to make reservations
        TotalReservationDTO totalReservationDTO = reservationService.createReservation(reservationDTOs);
        TotalReservationDTO totalReservationDTO2 = reservationService.createReservation(reservationDTOs2);
        // Display confirmation or further processing
        System.out.println("Reservations created successfully! Total Reservation ID: " + totalReservationDTO.getId());
        System.out.println("Total Price: kr " + totalReservationDTO.getTotalPrice());
    }


//
//    public void initReservations() {
//        // Retrieve screenings and seats for testing
//        Screening screening1 = screeningRepository.findById(1).orElseThrow(() ->
//                new RuntimeException("Screening not found"));
//        Seat seat1 = seatRepository.findById(1).orElseThrow(() ->
//                new RuntimeException("Seat not found"));
//        Screening screening2 = screeningRepository.findById(2).orElseThrow(() ->
//                new RuntimeException("Screening not found"));
//        Seat seat2 = seatRepository.findById(2).orElseThrow(() ->
//                new RuntimeException("Seat not found"));
//
//        // Check if the seats are valid for the screenings' screens
//        if (!screening1.isSeatValidForScreen(seat1.getId()) || !screening2.isSeatValidForScreen(seat2.getId())) {
//            throw new RuntimeException("Seat is not valid for the screening's screen");
//        }
//
////        // Calculate reservation prices using TotalReservationService
////        double price1 = totalReservationService.calculateTotalPrice(screening1, screening1.getMovie(), List.of(seat1));
////        double price2 = totalReservationService.calculateTotalPrice(screening2, screening2.getMovie(), List.of(seat2));
//
////        // Print out reservation prices
////        System.out.println("Reservation Price 1: " + price1);
////        System.out.println("Reservation Price 2: " + price2);
////
////        // Create reservations
////        Reservation reservation1 = new Reservation(screening1, seat1, "TestUser", price1);
////        Reservation reservation2 = new Reservation(screening2, seat2, "TestUser", price2);
////
////        // Save reservations
////        reservationRepository.saveAll(List.of(reservation1, reservation2));
////        System.out.println("Reservations updated successfully.");
////    }
//
//    public void initTotalReservation() {
//        TotalReservation totalReservation1 = new TotalReservation();
//        totalReservationRepository.save(totalReservation1);
//    }
//
//    public void addReservationsToTotalReservationAndUpdate() {
//        // Retrieve reservations by their IDs
//        Reservation reservation1 = reservationRepository.findById(1)
//                .orElseThrow(() -> new RuntimeException("Reservation with ID 1 not found"));
//        Reservation reservation2 = reservationRepository.findById(2)
//                .orElseThrow(() -> new RuntimeException("Reservation with ID 2 not found"));
//
//        // Retrieve the TotalReservation entity (assuming ID 1)
//        TotalReservation totalReservation = totalReservationRepository.findById(1)
//                .orElseThrow(() -> new RuntimeException("TotalReservation with ID 1 not found"));
//
//        // Add reservations to TotalReservation
//        totalReservation.addReservation(reservation1);
//        totalReservation.addReservation(reservation2);
//
//        // Calculate total price after adding reservations
//        totalReservation.calculateTotalPrice();
//
//        // Update the TotalReservation reference in reservations
//        reservation1.setTotalReservation(totalReservation);
//        reservation2.setTotalReservation(totalReservation);
//
//        // Save the updated reservations
//        reservationRepository.saveAll(List.of(reservation1, reservation2));
//
//        // Save the updated TotalReservation
//        totalReservationRepository.save(totalReservation);
//
//        System.out.println("TotalReservations updated successfully.");
//    }

    // RESERVATION WITH NO CHECK
    //    public void initReservations(){
    //        // Retrieve a screening and a seat for testing
    //        Screening screening = screeningRepository.findById(1).orElseThrow(() ->
    //                new RuntimeException("Screening not found"));
    //        Seat seat = seatRepository.findById(45).orElseThrow(() ->
    //                new RuntimeException("Seat not found"));
    //
    //        // Create a reservation for the screening and seat
    //        Reservation reservation = new Reservation(screening, seat, "TestUser");
    //        reservationRepository.save(reservation);
    //    }


}
