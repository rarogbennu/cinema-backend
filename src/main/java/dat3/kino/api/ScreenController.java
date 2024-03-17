package dat3.kino.api;

import dat3.kino.dto.ScreenDTO;
import dat3.kino.service.CinemaService;
import dat3.kino.service.ScreenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/screens")
public class ScreenController {

    private final ScreenService screenService;

    public ScreenController(ScreenService screenService) {
        this.screenService = screenService;
    }

    @Operation(summary = "Get all screens", description = "Get a list of all screens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all screens"),
            @ApiResponse(responseCode = "404", description = "No screens found")
    })
    @GetMapping
    public List<ScreenDTO> getAllScreens(@Parameter(description = "Optional cinema name to filter screens") @RequestParam(required = false) String cinema) {
        if (cinema != null) {
            System.out.println(cinema);
        }
        return screenService.getAllScreens();
    }

    @Operation(summary = "Get screen by ID", description = "Get a screen by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the screen"),
            @ApiResponse(responseCode = "404", description = "Screen not found")
    })
    @GetMapping("/{id}")
    public ScreenDTO getScreenById(@PathVariable int id) {
        return screenService.getScreenById(id);
    }
}
