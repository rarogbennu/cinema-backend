package dat3.kino.api;

import dat3.kino.dto.SeatDTO;
import dat3.kino.service.SeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @Operation(summary = "Get all seats", description = "Get a list of all seats")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all seats"),
            @ApiResponse(responseCode = "404", description = "No seats found")
    })
    @GetMapping
    public List<SeatDTO> getAllSeats() {
        return seatService.getAllSeats();
    }

    @Operation(summary = "Get seat by ID", description = "Get a seat by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the seat"),
            @ApiResponse(responseCode = "404", description = "Seat not found")
    })
    @GetMapping(path = "/{id}")
    public SeatDTO getSeatById(@PathVariable int id) {
        return seatService.getSeatById(id);
    }
}

