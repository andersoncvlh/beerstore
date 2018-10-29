package com.oak.beerstore.beerstore.models;

import com.oak.beerstore.beerstore.enumerations.BeerType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity(name = "beer")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Beer {

    @Id
    @SequenceGenerator(name = "seq_beer_id", sequenceName = "seq_beer_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_beer_id")
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private BeerType type;
    private BigDecimal volume;

}
