package dat3.kino.api;

import dat3.kino.dto.ScreenDTO;
import dat3.kino.service.CinemaService;
import dat3.kino.service.ScreenService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/screens")
public class ScreenController {

    private final ScreenService screenService;

    public ScreenController(ScreenService screenService) {
        this.screenService = screenService;
    }

    @GetMapping
    public List<ScreenDTO> getAllScreens(@RequestParam(required = false) String Cinema) {
        if (Cinema != null) {
            System.out.println(Cinema);
        }
        return screenService.getAllScreens();
    }

    @GetMapping(path= "/{id}")
    public ScreenDTO getScreensById(@PathVariable int id) {
        return screenService.getScreenById(id);
    }
}
