package dat3.kino.repository;

import dat3.kino.entity.PriceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceCategoryRepository extends JpaRepository<PriceCategory, Integer> {
}
