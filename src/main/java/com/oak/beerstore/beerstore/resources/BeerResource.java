package com.oak.beerstore.beerstore.resources;

import com.oak.beerstore.beerstore.models.Beer;
import com.oak.beerstore.beerstore.repositories.BeersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/beers")
public class BeerResource {

    @Autowired
    private BeersRepository beersRepository;

    @GetMapping
    public ResponseEntity<List<Beer>> all() {
        return ResponseEntity.ok(beersRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Beer> create(@RequestBody Beer beer) {
        Beer beerSaved = beersRepository.save(beer);
        return ResponseEntity.created(URI.create(beer.getId().toString())).body(beerSaved);
    }

}
