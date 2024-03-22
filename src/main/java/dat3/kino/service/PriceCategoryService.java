package dat3.kino.service;

import dat3.kino.entity.PriceCategory;
import dat3.kino.repository.PriceCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This service class provides methods to retrieve price categories.
 */
@Service
public class PriceCategoryService {

    private final PriceCategoryRepository priceCategoryRepository;

    /**
     * Constructs a new PriceCategoryService with the specified repository.
     *
     * @param priceCategoryRepository The repository for accessing price category data.
     */
    @Autowired
    public PriceCategoryService(PriceCategoryRepository priceCategoryRepository) {
        this.priceCategoryRepository = priceCategoryRepository;
    }

    /**
     * Retrieves all price categories.
     *
     * @return A list of all price categories.
     */
    public List<PriceCategory> getAllPriceCategories() {
        return priceCategoryRepository.findAll();
    }
}
