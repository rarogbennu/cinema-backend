package dat3.kino.service;

import dat3.kino.entity.PriceCategory;
import dat3.kino.repository.PriceCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceCategoryService {

    private final PriceCategoryRepository priceCategoryRepository;

    @Autowired
    public PriceCategoryService(PriceCategoryRepository priceCategoryRepository) {
        this.priceCategoryRepository = priceCategoryRepository;
    }

    public List<PriceCategory> getAllPriceCategories() {
        return priceCategoryRepository.findAll();
    }

    // You can add more service methods here if needed
}