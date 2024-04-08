package dat3.kino.pricing;

import org.springframework.stereotype.Service;

@Service
public class PricingManager {
    private PricingStrategy pricingStrategy;

    public void setPricingStrategy(PricingStrategy pricingStrategy){
        this.pricingStrategy = pricingStrategy;
    }
}
