package dat3.kino.pricing.priceCategory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "price_category")
public class PriceCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "3d_cost")
    private double additional3DCost;

    @Column(name = "long_movie_cost")
    private double additionalLongMovieCost;

    public PriceCategory(String name, double price, double additional3DCost, double additionalLongMovieCost) {
        this.name = name;
        this.price = price;
        this.additional3DCost = additional3DCost;
        this.additionalLongMovieCost = additionalLongMovieCost;
    }
}
