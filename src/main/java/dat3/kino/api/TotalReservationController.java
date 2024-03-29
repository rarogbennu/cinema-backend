package dat3.kino.api;

import dat3.kino.dto.TotalReservationDTO;
import dat3.kino.service.TotalReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/total-reservations")
public class TotalReservationController {

    private final TotalReservationService totalReservationService;

    public TotalReservationController(TotalReservationService totalReservationService) {
        this.totalReservationService = totalReservationService;
    }

    @Operation(summary = "Get all totalReservations", description = "Get a list of all totalReservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all totalReservations"),
            @ApiResponse(responseCode = "404", description = "No totalReservations found")
    })
    @GetMapping
    public ResponseEntity<List<TotalReservationDTO>> getAllTotalReservations() {
        List<TotalReservationDTO> totalReservations = totalReservationService.getAllTotalReservations();
        return new ResponseEntity<>(totalReservations, HttpStatus.OK);
    }

    @Operation(summary = "Get totalReservation by ID", description = "Get a totalReservation by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the totalReservation"),
            @ApiResponse(responseCode = "404", description = "TotalReservation not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TotalReservationDTO> getTotalReservationById(@PathVariable int id) {
        TotalReservationDTO totalReservationDTO = totalReservationService.getTotalReservationById(id);
        return new ResponseEntity<>(totalReservationDTO, HttpStatus.OK);
    }

//    @PostMapping
//    public ResponseEntity<TotalReservationDTO> createTotalReservation(@RequestBody TotalReservationDTO totalReservationDTO) {
//        TotalReservationDTO createdTotalReservationDTO = totalReservationService.createTotalReservation(totalReservationDTO);
//        return new ResponseEntity<>(createdTotalReservationDTO, HttpStatus.CREATED);
//    }

    @Operation(summary = "Delete a totalReservation", description = "Delete a totalReservation by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "TotalReservation deleted"),
            @ApiResponse(responseCode = "404", description = "TotalReservation not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTotalReservation(@PathVariable int id) {
        totalReservationService.deleteTotalReservation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
