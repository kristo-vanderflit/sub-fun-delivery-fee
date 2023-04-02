package com.fujitsu.deliveryfee.City;

import java.util.List;
import java.util.Optional;

public interface CityService {

    City save(City city);
    void deleteCityById(Integer id);
    boolean isCityExist(Integer id);
    List<City> findAllCities();
    Optional<City> findCityByName(String cityName);
    String calculationFee(String city, String vehicle);

}
