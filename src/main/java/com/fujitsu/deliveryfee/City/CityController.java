package com.fujitsu.deliveryfee.City;

import com.fujitsu.deliveryfee.Exception.CityNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/cities")
@Tag(name = "City", description = "Methods working with the city and calculating delivery fee")
public class CityController {
    private final CityService cityService;
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    /**
     * Adds the city
     * @param city city entity
     * @return city entity
     */
    /*@PostMapping
    @Operation(summary = "Adding a new city")
    public ResponseEntity<City> addCity(@RequestBody City city) {

        if (cityService.findCityByName(city.getName().toLowerCase()).isPresent() || city.getId() == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(cityService.save(city), HttpStatus.CREATED);
    }*/
    @PostMapping
    @Operation(summary = "Adding a new city")
    public ResponseEntity<City> addCity(@RequestBody City city) {

        if (cityService.isCityExist((int) city.getId()) || city.getId() == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(cityService.save(city), HttpStatus.CREATED);
    }

    /**
     * Finds all cities
     * @return list of cities
     */
    @GetMapping
    @Operation(summary = "Getting a list of cities")
    public ResponseEntity<List<City>> findAllCities() {
        return new ResponseEntity<>(cityService.findAllCities(), HttpStatus.OK);
    }

    /**
     * Finds the city by city name
     * @param city city name
     * @return the city entity
     */
    @GetMapping("{city}")
    @Operation(summary = "Getting city by its name")
    public City findCity(@PathVariable String city) {
        return cityService.findCityByName(city.toLowerCase()).orElseThrow(() ->
                new CityNotFoundException(String.format("City not found: %s", city)));
    }

    /**
     * Calculates fee based on city and vehicle type
     * @param city city name
     * @param vehicle vehicle type
     * @return calculated fee
     */
    @GetMapping("{city}/{vehicle}")
    @Operation(summary = "Calculates delivery fee by city name and vehicle type",
            description = "vehicle types: car, bike, scooter.")
    public ResponseEntity<String> calculateFee(@PathVariable String city, @PathVariable String vehicle) {

        if (cityService.findCityByName(city.toLowerCase()).isPresent()) {
            return new ResponseEntity<>(cityService.calculationFee(city, vehicle.toLowerCase()), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Deletes the city by id
     * @param id city id
     */
    @DeleteMapping("{id}")
    @Operation(summary = "Deleting city by ID")
    public void deleteCity(@PathVariable Integer id) {
        cityService.deleteCityById(id);
    }
}
