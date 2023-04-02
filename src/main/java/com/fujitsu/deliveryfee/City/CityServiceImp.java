package com.fujitsu.deliveryfee.City;

import com.fujitsu.deliveryfee.Exception.CityNotFoundException;
import com.fujitsu.deliveryfee.Exception.VehicleTypeForbiddenException;
import com.fujitsu.deliveryfee.Exception.WrongVehicleTypeException;
import com.fujitsu.deliveryfee.Station.Station;
import com.fujitsu.deliveryfee.Station.StationRepository;
import com.fujitsu.deliveryfee.Station.StationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class CityServiceImp implements CityService {

    private final CityRepository cityRepository;
    private final StationRepository stationRepository;
    private final StationService stationService;

    @Autowired
    public CityServiceImp(CityRepository cityRepository, StationRepository stationRepository, StationService stationService) {
        this.cityRepository = cityRepository;
        this.stationRepository = stationRepository;
        this.stationService = stationService;
    }

    /**
     * Saves entity of city
     */
    @Override
    public City save(City city) {
        return cityRepository.save(city);
    }

    /**
     * Removes entity city by its id
     * @param id city id
     */
    @Override
    public void deleteCityById(Integer id) {
        cityRepository.deleteById(id);
    }

    /**
     * Checks is city exist
     * @param id entity id
     * @return true if city exist otherwise false
     */
    @Override
    public boolean isCityExist(Integer id) {
        return cityRepository.existsById(id);
    }

    /**
     * Finds all cities
     * @return list of existing cities
     */
    @Override
    public List<City> findAllCities() {
        return cityRepository.findAll();
    }

    /**
     * Searching city by name
     * @param city given city name
     * @return city entity or optional
     */
    @Override
    public Optional<City> findCityByName(String city) {
        return cityRepository.findAll().stream()
                .filter(x -> x.getName().equals(city))
                .findFirst();
    }

    /**
     * Calculates delivery fee
     * @param city given name of city
     * @param vehicleType given vehicle type of current city
     * @return calculated fee based on regional base fee and extra weather fee
     */
    @Override
    public String calculationFee(String city, String vehicleType) {

        String vehicle = vehicleType.toLowerCase();

        Optional<City> city1 = cityRepository.findAll().stream().filter(x -> x.getName().toLowerCase().contains(city.toLowerCase())).findFirst();
        double baseFeeResult = city1.map(value -> getRegionalBaseFee(value, vehicle)).orElseThrow(() -> new CityNotFoundException("City : " + city + " not found"));

        String timestamp = stationService.getLastTimestamp();
        Optional<Station> station = stationRepository.findAll().stream()
                .filter(s -> s.getTimestamp().equals(timestamp))
                .filter(s -> s.getWmocode() == city1.get().getStationCode())
                .findFirst();

        double extraResult = station.map(value -> getExtraFee(value, vehicle, city1.get())).orElse(0.0);
        return String.valueOf(baseFeeResult + extraResult);
    }

    /**
     * Calculates regional base fee by vehicle type
     * @param city city
     * @param vehicle vehicle type
     * @return calculated regional base fee or throw exception if vehicle type is not exist
     */
    public double getRegionalBaseFee(City city, String vehicle) {

        return switch (vehicle) {
            case "car" -> city.getRegionalBaseFee().getCar();
            case "bike" -> city.getRegionalBaseFee().getBike();
            case "scooter" -> city.getRegionalBaseFee().getScooter();
            //TODO change exception
            default -> throw new WrongVehicleTypeException("Wrong vehicle type :" + vehicle);
        };
    }

    /**
     * Calculates extra fee by vehicle type and weather condition
     * @param station weather station
     * @param vehicle vehicle type
     * @param city city
     * @return extra fee of weather conditions or
     * throw exception if usage is forbidden of weather condition
     */
    public double getExtraFee(Station station, String vehicle, City city) {

        double airTemperatureFee = 0;
        double windSpeedFee = 0;
        double weatherPhenomenonFee = 0;

        switch (vehicle) {
            case "scooter", "bike" -> airTemperatureFee = getAirTemperatureFee(station, city);
        }

        if (vehicle.equals("bike")) {
            windSpeedFee = getWindSpeedFee(station, city);
        }

        switch (vehicle) {
            case "scooter", "bike" -> weatherPhenomenonFee = getWeatherPhenomenonFee(station, city);
        }


        return airTemperatureFee + windSpeedFee + weatherPhenomenonFee;
    }

    /**
     * Calculates fee by outside air temperature
     * @param station weather station
     * @param city city
     * @return value by weather condition
     */
    public double getAirTemperatureFee(Station station, City city) {
        double airTemperature = station.getAirTemperature();

        if (-10 > airTemperature) {
            return city.getExtraFee().getAirTemperature1();
        }

        if (-10 <= airTemperature && airTemperature <= 0) {
            return city.getExtraFee().getAirTemperature2();
        }

        return 0;
    }

    /**
     * Calculates fee by wind speed
     * @param station weather station
     * @param city city
     * @return value by weather condition or throw exception if wind speed is extremely high
     */
    public double getWindSpeedFee(Station station, City city) {
        double windSpeed = station.getWindSpeed();

        if (windSpeed > 20) {
            throw new VehicleTypeForbiddenException("Usage of selected vehicle type is forbidden");
        }

        if (10 <= windSpeed && windSpeed <= 20) {
            return city.getExtraFee().getWindSpeed1();
        }

        return 0;
    }

    /**
     * Calculates fee by weather phenomenon
     * @param station weather station
     * @param city city
     * @return value by weather condition or throw exception if weather conditions do not allow for delivery
     */
    public double getWeatherPhenomenonFee(Station station, City city) {

        String phenomenon = station.getPhenomenon().toLowerCase();

        if (phenomenon.contains("snow") || phenomenon.contains("sleet")) {
            return city.getExtraFee().getPhenomenon1();
        }

        if (phenomenon.contains("rain")) {
            return city.getExtraFee().getPhenomenon2();
        }

        return switch (phenomenon) {
            //TODO what about thunderstorm?
            case "glaze", "hail", "thunder" -> throw new VehicleTypeForbiddenException("Usage of selected vehicle type is forbidden");
            default -> 0;
        };
    }
}
