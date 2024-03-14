package dat3.kino.configuration;

import dat3.kino.entity.*;
import dat3.kino.repository.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class InitDataReservation implements ApplicationRunner {
    private final MovieRepository movieRepository;
    private final CinemaRepository cinemaRepository;
    private final ScreenRepository screenRepository;
    private final ScreeningRepository screeningRepository;
    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;
    private final TotalReservationRepository totalReservationRepository;


    public InitDataReservation(MovieRepository movieRepository,
                               CinemaRepository cinemaRepository,
                               ScreenRepository screenRepository,
                               ScreeningRepository screeningRepository,
                               ReservationRepository reservationRepository, SeatRepository seatRepository, TotalReservationRepository totalReservationRepository) {
        this.movieRepository = movieRepository;
        this.cinemaRepository = cinemaRepository;
        this.screenRepository = screenRepository;
        this.screeningRepository = screeningRepository;
        this.reservationRepository = reservationRepository;
        this.seatRepository = seatRepository;
        this.totalReservationRepository = totalReservationRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        initMovies();
        initScreenings();
        initReservations();
        initTotalReservation();

    }

    public void initMovies() {
        Duration durationAvatar = Duration.ofHours(2).plusMinutes(30);
        Movie movie1 = new Movie("Avatar", durationAvatar, true);
        movieRepository.save(movie1);

        Duration durationTheSeventhSeal = Duration.ofHours(3).plusMinutes(30);
        Movie movie2 = new Movie("The Seventh Seal", durationTheSeventhSeal, false);
        movieRepository.save(movie2);

        Duration durationDudeWheresMyCar = Duration.ofHours(1).plusMinutes(30);
        Movie movie3 = new Movie("Dude, Where's My Car?", durationDudeWheresMyCar, false);
        movieRepository.save(movie3);
    }

    public void initScreenings() {
        Movie movie1 = movieRepository.findByName("Avatar").orElseThrow();
        Movie movie2 = movieRepository.findByName("The Seventh Seal").orElseThrow();
        Movie movie3 = movieRepository.findByName("Dude, Where's My Car?").orElseThrow();

        Cinema cinemaCopenhagen = cinemaRepository.findByName("Cinema Copenhagen").orElseThrow();
        Cinema cinemaRoskilde = cinemaRepository.findByName("Cinema Roskilde").orElseThrow();
        Screen screen1 = screenRepository.findByName("Screen 1").orElseThrow();
        Screen screen2 = screenRepository.findByName("Screen 2").orElseThrow();

        LocalDateTime now = LocalDateTime.now();

        Screening screening1 = new Screening(now, movie1, cinemaCopenhagen, screen1);
        Screening screening2 = new Screening(now.plusDays(1), movie2, cinemaCopenhagen, screen1);
        Screening screening3 = new Screening(now.plusDays(2), movie3, cinemaRoskilde, screen2);

        screeningRepository.saveAll(List.of(screening1, screening2, screening3));
    }

    public void initReservations() {
        // Retrieve a screening and a seat for testing
        Screening screening = screeningRepository.findById(1).orElseThrow(() ->
                new RuntimeException("Screening not found"));
        Seat seat = seatRepository.findById(1).orElseThrow(() ->
                new RuntimeException("Seat not found"));

        // Check if the seat is valid for the screening's screen
        if (!screening.isSeatValidForScreen(seat.getId())) {
            throw new RuntimeException("Seat is not valid for the screening's screen");
        }

        // Create a reservation for the screening and seat
        Reservation reservation = new Reservation(screening, seat, "TestUser");
        reservationRepository.save(reservation);

        Screening screening1 = screeningRepository.findById(1).orElseThrow(() ->
                new RuntimeException("Screening not found"));
        Seat seat1 = seatRepository.findById(2).orElseThrow(() ->
                new RuntimeException("Seat not found"));

        if (!screening1.isSeatValidForScreen(seat1.getId())) {
            throw new RuntimeException("Seat is not valid for the screening's screen");
        }

        // Create a reservation for the screening and seat
        Reservation reservation1 = new Reservation(screening1, seat1, "TestUser1");
        reservationRepository.save(reservation1);
    }

    public void initTotalReservation() {
        // Retrieve the reservations from the database
        System.out.println("Retrieving reservations from the database...");
        Reservation reservation1 = reservationRepository.findById(1).orElseThrow(() ->
                new RuntimeException("Reservation with ID 1 not found"));
        System.out.println("Reservation 1 retrieved: " + reservation1);

        Reservation reservation2 = reservationRepository.findById(2).orElseThrow(() ->
                new RuntimeException("Reservation with ID 2 not found"));
        System.out.println("Reservation 2 retrieved: " + reservation2);

        // Create a total reservation and add the reservations to it
        System.out.println("Creating TotalReservation object...");
        TotalReservation totalReservation = new TotalReservation();
        System.out.println("TotalReservation object created: " + totalReservation);

        totalReservation.addReservation(reservation1);
        System.out.println("Reservation 1 added to TotalReservation: " + totalReservation);

        totalReservation.addReservation(reservation2);
        System.out.println("Reservation 2 added to TotalReservation: " + totalReservation);

        // Save the total reservation to the database
        System.out.println("Saving TotalReservation to the database...");
        totalReservationRepository.save(totalReservation);
        System.out.println("TotalReservation saved successfully.");
    }



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
