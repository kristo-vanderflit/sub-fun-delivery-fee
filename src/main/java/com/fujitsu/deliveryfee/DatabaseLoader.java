package com.fujitsu.deliveryfee;

import com.fujitsu.deliveryfee.BaseFee.RegionalBaseFee;
import com.fujitsu.deliveryfee.BaseFee.RegionalBaseFeeRepository;
import com.fujitsu.deliveryfee.City.City;
import com.fujitsu.deliveryfee.City.CityRepository;
import com.fujitsu.deliveryfee.ExtraFee.ExtraFee;
import com.fujitsu.deliveryfee.ExtraFee.ExtraFeeRepository;
import com.fujitsu.deliveryfee.Station.StationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DatabaseLoader {
/**
     * Use Spring to inject a {@link CityRepository}, {@link ExtraFeeRepository}, {@link RegionalBaseFeeRepository} that can then load data. Since this will run only after the app
     * is operational, the database will be up.
     **/
    @Bean
    CommandLineRunner init(CityRepository cityRepository,
                           RegionalBaseFeeRepository regionalBaseFeeRepository) {
        return args -> {

            regionalBaseFeeRepository.save(new RegionalBaseFee(1, 4,3.5,3));
            regionalBaseFeeRepository.save(new RegionalBaseFee(2, 3.5,3,2.5));
            regionalBaseFeeRepository.save(new RegionalBaseFee(3, 3,2.5,2));

            ExtraFee extraFee1 = new ExtraFee(1,122,17,19,100,1);
            ExtraFee extraFee2 = new ExtraFee(2,122,1213,1,1,1);
            ExtraFee extraFee3 = new ExtraFee(3,14,16,1,1321,100);

            City city1 = new City(1, "Tallinn", 26038);
            City city2 = new City(2, "Tartu", 26242);
            City city3 = new City(3, "Pärnu", 41803);

            city1.setRegionalBaseFee(regionalBaseFeeRepository.findById(1L).get());
            city2.setRegionalBaseFee(regionalBaseFeeRepository.findById(2L).get());
            city3.setRegionalBaseFee(regionalBaseFeeRepository.findById(3L).get());

            city1.setExtraFee(extraFee1);
            city2.setExtraFee(extraFee2);
            city3.setExtraFee(extraFee3);

            List<City> list = Arrays.asList(city1, city2, city3);
            cityRepository.saveAll(list);
        };
    }

}
