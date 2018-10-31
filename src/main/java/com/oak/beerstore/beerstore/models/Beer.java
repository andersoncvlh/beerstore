package com.oak.beerstore.beerstore.models;

import com.oak.beerstore.beerstore.enumerations.BeerType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotBlank(message = "beers-1")
    private String name;
    @NotNull(message = "beers-2")
    private BeerType type;
    @NotNull(message = "beers-3")
    @DecimalMin(value = "0", message = "beers-4")
    private BigDecimal volume;

}
