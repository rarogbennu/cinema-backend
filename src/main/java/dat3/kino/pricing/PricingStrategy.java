package dat3.kino.pricing;

import dat3.kino.screen.Screen;
import dat3.kino.screening.Screening;
import dat3.kino.seat.Seat;

public interface PricingStrategy {
    double getPriceCategory(Seat seat);
    double getScreeningAdjustment(Screening screening);

}
