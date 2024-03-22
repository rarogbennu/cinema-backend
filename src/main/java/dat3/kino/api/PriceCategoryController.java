package dat3.kino.api;

import dat3.kino.entity.PriceCategory;
import dat3.kino.service.PriceCategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/price-categories")
public class PriceCategoryController {

    private final PriceCategoryService priceCategoryService;

    public PriceCategoryController(PriceCategoryService priceCategoryService) {
        this.priceCategoryService = priceCategoryService;
    }

    @GetMapping
    public List<PriceCategory> getAllPriceCategories() {
        return priceCategoryService.getAllPriceCategories();
    }
}