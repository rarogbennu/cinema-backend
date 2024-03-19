package dat3.kino.api;

import dat3.kino.dto.BookingDTO;
import dat3.kino.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(summary = "Get all bookings", description = "Get a list of all bookings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all bookings"),
            @ApiResponse(responseCode = "404", description = "No bookings found")
    })
    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        List<BookingDTO> bookings = bookingService.getAllBookings();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @Operation(summary = "Get booking by ID", description = "Get a booking by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the booking"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable int id) {
        BookingDTO bookingDTO = bookingService.getBookingById(id);
        return new ResponseEntity<>(bookingDTO, HttpStatus.OK);
    }

//    @PostMapping
//    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingDTO bookingDTO) {
//        BookingDTO createdBookingDTO = bookingService.createBooking(bookingDTO);
//        return new ResponseEntity<>(createdBookingDTO, HttpStatus.CREATED);
//    }

    @Operation(summary = "Delete a booking", description = "Delete a booking by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Booking deleted"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable int id) {
        bookingService.deleteBooking(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
