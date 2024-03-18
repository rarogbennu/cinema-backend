package dat3.kino.api;

import dat3.kino.dto.CinemaDTO;
import dat3.kino.service.CinemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cinemas")
@CrossOrigin(origins = "http://localhost:5174")
public class CinemaController {

    private final CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @Operation(summary = "Get all cinemas", description = "Get a list of all cinemas")
    @ApiResponse(responseCode = "200", description = "List of cinemas retrieved successfully")
    @GetMapping
    public List<CinemaDTO> getAllCinemas() {
        return cinemaService.getAllCinemas();
    }

    @Operation(summary = "Get cinema by ID", description = "Get details of a cinema by its ID")
    @ApiResponse(responseCode = "200", description = "Cinema details retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Cinema not found with the given ID")
    @GetMapping(path= "/{id}")
    public CinemaDTO getCinemaById(@PathVariable int id) {
        return cinemaService.getCinemaById(id);
    }
}