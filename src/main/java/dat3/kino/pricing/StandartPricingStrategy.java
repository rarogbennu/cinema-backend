package dat3.kino.pricing;

import dat3.kino.seat.Seat;

public class StandartPricingStrategy implements PricingStrategy {
    public double getSeatPriceCategory(Seat seat){
        String seatCategory = seat.getPriceCategory();

        return seatCategory;

    }
}
