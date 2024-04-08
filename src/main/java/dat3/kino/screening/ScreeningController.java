package dat3.kino.screening;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/screenings")
public class ScreeningController {

    private final ScreeningService screeningService;

    public ScreeningController(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    @Operation(summary = "Get all screenings", description = "Get a list of all screenings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all screenings"),
            @ApiResponse(responseCode = "404", description = "No screenings found")
    })
    @GetMapping
    public List<ScreeningDTO> getAllScreenings() {
        return screeningService.getAllScreenings();
    }

    @Operation(summary = "Get screening by ID", description = "Get a screening by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the screening"),
            @ApiResponse(responseCode = "404", description = "Screening not found")
    })
    @GetMapping("/{id}")
    public ScreeningDTO getScreeningById(@PathVariable int id) {
        return screeningService.getScreeningById(id);
    }

    @Operation(summary = "Get screenings by cinema ID", description = "Get a list of screenings for a specific cinema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found screenings for the cinema"),
            @ApiResponse(responseCode = "404", description = "No screenings found for the cinema")
    })
    @GetMapping("/cinema/{cinemaId}")
    public List<ScreeningDTO> getScreeningsByCinemaId(@PathVariable int cinemaId) {
        return screeningService.getScreeningsByCinemaId(cinemaId);
    }

    @Operation(summary = "Create a new screening", description = "Create a new screening")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Screening created successfully")
    })
    @PostMapping
    public ScreeningDTO createScreening(@RequestBody ScreeningDTO screeningDTO) {
        return screeningService.createScreening(screeningDTO);
    }

    @Operation(summary = "Delete a screening", description = "Delete a screening by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Screening deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Screening not found")
    })
    @DeleteMapping("/{id}")
    public void deleteScreening(@PathVariable int id) {
        screeningService.deleteScreening(id);
    }
}

