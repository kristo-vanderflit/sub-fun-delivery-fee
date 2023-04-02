package com.fujitsu.deliveryfee.City;

import com.fujitsu.deliveryfee.BaseFee.RegionalBaseFee;
import com.fujitsu.deliveryfee.Exception.VehicleTypeForbiddenException;
import com.fujitsu.deliveryfee.Exception.WrongVehicleTypeException;
import com.fujitsu.deliveryfee.ExtraFee.ExtraFee;
import com.fujitsu.deliveryfee.Station.Station;
import com.fujitsu.deliveryfee.Station.StationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class CityServiceImpTest {

    @Autowired
    private CityServiceImp cityServiceImp;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CityService cityService;
    @Autowired
    private StationRepository stationRepository;

    @BeforeEach
    void cleanup() {
        cityRepository.deleteAll();
        stationRepository.deleteAll();
    }

    @Test
    void shouldGiveCityByName() {
        List<City> cities = getCities();
        cities.forEach(cityServiceImp::save);

        String city = "Tartu";
        Optional<City> actualResult = cityServiceImp.findCityByName(city);

        Assertions.assertTrue(actualResult.isPresent());
        Assertions.assertEquals(city, actualResult.get().getName());
    }

    @Test
    void shouldNotGiveCityByName() {
        List<City> cities = getCities();
        cities.forEach(cityServiceImp::save);

        String city = "Rakvere";

        Optional<City> actualResult = cityServiceImp.findCityByName(city);
        Assertions.assertFalse(actualResult.isPresent());
    }

    @Test
    void calculationFee() {

        String city1 = "tartu";
        String vehicle1 = "bike";
        String vehicle2 = "BIKE";
        String vehicle3 = "Scooter";
        String wrongVehicleType = "test";

        City tartu = new City(2, "tartu", 2345,
                new RegionalBaseFee(2,3.5,3,2.5),
                new ExtraFee(2,1,0.5,0.5,1,0.5));

        Station station = new Station(2, "Tartu-Tõravere", 2345,
                -2.1,4.7,"Light snow shower", "1234567890");

        cityServiceImp.save(tartu);
        stationRepository.save(station);

        String expected = "4.0";

        String result1 = cityService.calculationFee(city1, vehicle1);
        String result2 = cityService.calculationFee(city1, vehicle2);
        String result3 = cityService.calculationFee(city1, vehicle3);

        Assertions.assertEquals(expected, result1);
        Assertions.assertEquals(expected, result2);
        Assertions.assertEquals("4.5", result3);

        Assertions.assertThrows(WrongVehicleTypeException.class, () -> {
            cityService.calculationFee(city1, wrongVehicleType);
        });
    }

    @Test
    void getRegionalBaseFeeOrThrowException() {
        List<City> cities = getCities();
        cities.forEach(cityServiceImp::save);

        String vehicleCar = "car";
        String vehicleScooter = "scooter";
        String vehicleBike = "bike";
        String wrongVehicleType = "airplane";

        double actualCar = 2;
        double actualScooter = 3;
        double actualBike = 4;

        double resultCar = cityServiceImp.getRegionalBaseFee(cities.get(0), vehicleCar);
        double resultScooter = cityServiceImp.getRegionalBaseFee(cities.get(0), vehicleScooter);
        double resultBike = cityServiceImp.getRegionalBaseFee(cities.get(0), vehicleBike);

        Assertions.assertEquals(actualCar, resultCar);
        Assertions.assertEquals(actualScooter, resultScooter);
        Assertions.assertEquals(actualBike, resultBike);

        Assertions.assertThrows(WrongVehicleTypeException.class, () -> {
            cityServiceImp.getRegionalBaseFee(cities.get(0), wrongVehicleType);
        });
    }

    @Test
    void shouldGetCorrectAirTemperatureFee() {
        //getAirTemperatureFee(Station station, City city)
        List<City> cities = getCities();
        cities.forEach(cityServiceImp::save);
        //cities.get(0) -> EXTRA fees -> air1 = 2, air2 = 3, ws = 4, phenomenon1 = 5, phenomenon2 = 6

        Station station1 = new Station(1, "Tartu", 2345,-11,5,"", "1234567890");
        Station station2 = new Station(1, "Tartu", 2345,-9,5,"", "1234567890");
        Station station3 = new Station(1, "Tartu", 2345,2,5,"", "1234567890");
        Station station4 = new Station(1, "Tartu", 2345,0,5,"", "1234567890");
        Station station5 = new Station(1, "Tartu", 2345,-10,5,"", "1234567890");
        Station station6 = new Station(1, "Tartu", 2345,-5,5,"", "1234567890");

        double result1 = cityServiceImp.getAirTemperatureFee(station1, cities.get(0));
        double result2 = cityServiceImp.getAirTemperatureFee(station2, cities.get(0));
        double result3 = cityServiceImp.getAirTemperatureFee(station3, cities.get(0));
        double result4 = cityServiceImp.getAirTemperatureFee(station4, cities.get(0));
        double result5 = cityServiceImp.getAirTemperatureFee(station5, cities.get(0));
        double result6 = cityServiceImp.getAirTemperatureFee(station6, cities.get(0));

        Assertions.assertEquals(2, result1);
        Assertions.assertEquals(3, result2);
        Assertions.assertEquals(0, result3);
        Assertions.assertEquals(3, result4);
        Assertions.assertEquals(3, result5);
        Assertions.assertEquals(3, result6);
    }

    @Test
    void shouldGetCorrectGetWindSpeedFeeOrThrowException() {
        List<City> cities = getCities();
        cities.forEach(cityServiceImp::save);

        Station station1 = new Station(1, "Tartu", 2345,-11,21,"", "1234567890");
        Station station2 = new Station(1, "Tartu", 2345,-11,10,"", "1234567890");
        Station station3 = new Station(1, "Tartu", 2345,-11,15,"", "1234567890");
        Station station4 = new Station(1, "Tartu", 2345,-11,20,"", "1234567890");
        Station station5 = new Station(1, "Tartu", 2345,-11,9,"", "1234567890");

        double result2 = cityServiceImp.getWindSpeedFee(station2, cities.get(0));
        double result3 = cityServiceImp.getWindSpeedFee(station3, cities.get(0));
        double result4 = cityServiceImp.getWindSpeedFee(station4, cities.get(0));
        double result5 = cityServiceImp.getWindSpeedFee(station5, cities.get(0));

        Assertions.assertThrows(VehicleTypeForbiddenException.class, () -> {
            cityServiceImp.getWindSpeedFee(station1, cities.get(0));
        });

        Assertions.assertEquals(4, result2);
        Assertions.assertEquals(4, result3);
        Assertions.assertEquals(4, result4);
        Assertions.assertEquals(0, result5);
    }

    @Test
    void shouldGetWeatherPhenomenonFeeOrThrowException() {

        List<City> cities = getCities();
        cities.forEach(cityServiceImp::save);

        Station station1 = new Station(1, "Tartu", 2345,-11,21,"snow", "1234567890");
        Station station2 = new Station(1, "Tartu", 2345,-11,21,"SLEET", "1234567890");
        Station station3 = new Station(1, "Tartu", 2345,-11,21,"Rain", "1234567890");
        Station station4 = new Station(1, "Tartu", 2345,-11,21,"", "1234567890");
        //Exception
        Station station5 = new Station(1, "Tartu", 2345,-11,21,"glaZe", "1234567890");
        Station station6 = new Station(1, "Tartu", 2345,-11,21,"HAIL", "1234567890");
        Station station7 = new Station(1, "Tartu", 2345,-11,21,"thunDER", "1234567890");


        double result1 = cityServiceImp.getWeatherPhenomenonFee(station1, cities.get(0));
        double result2 = cityServiceImp.getWeatherPhenomenonFee(station2, cities.get(0));
        double result3 = cityServiceImp.getWeatherPhenomenonFee(station3, cities.get(0));
        double result4 = cityServiceImp.getWeatherPhenomenonFee(station4, cities.get(0));

        Assertions.assertEquals(5, result1);
        Assertions.assertEquals(5, result2);
        Assertions.assertEquals(6, result3);
        Assertions.assertEquals(0, result4);

        Assertions.assertThrows(VehicleTypeForbiddenException.class, () -> {
            cityServiceImp.getWeatherPhenomenonFee(station5, cities.get(0));
        });

        Assertions.assertThrows(VehicleTypeForbiddenException.class, () -> {
            cityServiceImp.getWeatherPhenomenonFee(station6, cities.get(0));
        });

        Assertions.assertThrows(VehicleTypeForbiddenException.class, () -> {
            cityServiceImp.getWeatherPhenomenonFee(station7, cities.get(0));
        });
    }

    @Test
    void shouldGetRelatedToWeather() {

        List<City> cities = getCities();
        cities.forEach(cityServiceImp::save);

        Station station1 = new Station(1, "Tartu", 2345,-11,21,"rain", "1234567890");
        Station station2 = new Station(1, "Tartu", 2345,-11,21,"Light rain", "1234567890");
        Station station3 = new Station(1, "Tartu", 2345,-11,21,"Moderate rain", "1234567890");
        Station station4 = new Station(1, "Tartu", 2345,-11,21,"Heavy rain", "1234567890");

        //get phenomenon 2 -> 6 EUR
        double expected = 6.0;
        double result1 = cityServiceImp.getWeatherPhenomenonFee(station1, cities.get(0));
        double result2 = cityServiceImp.getWeatherPhenomenonFee(station2, cities.get(0));
        double result3 = cityServiceImp.getWeatherPhenomenonFee(station3, cities.get(0));
        double result4 = cityServiceImp.getWeatherPhenomenonFee(station4, cities.get(0));

        Assertions.assertEquals(expected, result1);
        Assertions.assertEquals(expected, result2);
        Assertions.assertEquals(expected, result3);
        Assertions.assertEquals(expected, result4);

    }

    @Test
    void isCityExist() {
        City city = getCities().get(0);
        cityServiceImp.save(city);
        boolean actual = cityServiceImp.isCityExist((int) city.getId());

        Assertions.assertTrue(actual);
    }

    @Test
    void isCityNotExist() {
        boolean actual = cityService.isCityExist(9090999);
        Assertions.assertFalse(actual);
    }

    private List<City> getCities() {
        //Don't use city: Rakvere
        City tartu = new City(1, "Tartu", 2345,
                new RegionalBaseFee(1,2,3,4),
                new ExtraFee(1,2,3,4,5,6));

        City tallinn = new City(2, "Tallinn", 23453,
                new RegionalBaseFee(2,3,5,4),
                new ExtraFee(2,3,4,5,5.2,6));

        return new ArrayList<>(Arrays.asList(tartu, tallinn));
    }
}