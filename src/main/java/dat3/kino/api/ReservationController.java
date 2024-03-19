package dat3.kino.api;

import dat3.kino.dto.ReservationDTO;
import dat3.kino.dto.TotalReservationDTO;
import dat3.kino.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(summary = "Get all reservations", description = "Get a list of all reservations")
    @GetMapping
    public List<ReservationDTO> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @Operation(summary = "Get reservation by ID", description = "Get a reservation by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the reservation"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    @GetMapping("/{id}")
    public ReservationDTO getReservationById(@PathVariable int id) {
        return reservationService.getReservationById(id);
    }

//    @Operation(summary = "Create a new reservation", description = "Create a new reservation")
//    @PostMapping
//    public ReservationDTO createReservation(@RequestBody ReservationDTO reservationDTO) {
//        return reservationService.createReservation(reservationDTO);
//    }

    @Operation(summary = "Create a new reservation or multiple reservations and group them into one total reservation", description = "Create a new reservation or multiple reservations and group them into one total reservation")
    @PostMapping
    public TotalReservationDTO createReservation(@RequestBody List<ReservationDTO> reservationDTOs) {
        return reservationService.createReservation(reservationDTOs);
    }



    @Operation(summary = "Delete a reservation", description = "Delete a reservation by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reservation deleted"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable int id) {
        reservationService.deleteReservation(id);
    }
}

