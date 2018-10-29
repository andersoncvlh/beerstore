package com.oak.beerstore.beerstore.repositories;

import com.oak.beerstore.beerstore.models.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeersRepository extends JpaRepository<Beer, Long> {
}
