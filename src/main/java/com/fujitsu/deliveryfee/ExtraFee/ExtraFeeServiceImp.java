package com.fujitsu.deliveryfee.ExtraFee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExtraFeeServiceImp implements ExtraFeeService {

    private final ExtraFeeRepository extraFeeRepository;

    public ExtraFeeServiceImp(ExtraFeeRepository extraFeeRepository) {
        this.extraFeeRepository = extraFeeRepository;
    }

    /**
     * Finds all extra fees
     * @return list of extra fees
     */
    @Override
    public List<ExtraFee> findAllFees() {
        return extraFeeRepository.findAll();
    }

    /**
     * Finds extra fee by id
     * @param id entity id
     * @return extra fee entity
     */
    @Override
    public Optional<ExtraFee> findFee(Long id) {
        return extraFeeRepository.findById(id);
    }

    /**
     * Changes extra fee values by id
     * @param id entity id
     * @param value extra fee values
     */
    @Override
    public void setExtraFeeValues(Long id, String value) {

        String[] values = value.split(",");
        Optional<ExtraFee> extraFee = extraFeeRepository.findById(id);

        if (values.length == 5 && extraFee.isPresent()) {
            try {
                extraFee.get().setAirTemperature1(Double.parseDouble(values[0]));
                extraFee.get().setAirTemperature2(Double.parseDouble(values[1]));
                extraFee.get().setWindSpeed1(Double.parseDouble(values[2]));
                extraFee.get().setPhenomenon1(Double.parseDouble(values[3]));
                extraFee.get().setPhenomenon2(Double.parseDouble(values[4]));

                extraFee.ifPresent(fee -> extraFeeRepository.save(fee));
            } catch (Exception e) {
                throw new NumberFormatException();
            }
        } else {
            throw new NumberFormatException();
        }

    }
}
