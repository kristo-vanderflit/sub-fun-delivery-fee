package com.fujitsu.deliveryfee.BaseFee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegionalBaseFeeServiceImp implements RegionalBaseFeeService {

    private final RegionalBaseFeeRepository regionalBaseFeeRepository;
    public RegionalBaseFeeServiceImp(RegionalBaseFeeRepository regionalBaseFeeRepository) {
        this.regionalBaseFeeRepository = regionalBaseFeeRepository;
    }

    /**
     * Saves entity RBF
     * @param regionalBaseFee entity
     */
    @Override
    public void save(RegionalBaseFee regionalBaseFee) {
        regionalBaseFeeRepository.save(regionalBaseFee);
    }

    /**
     * Finds entity RBF
     * @param id entity id
     * @return regional base fee entity
     */
    @Override
    public Optional<RegionalBaseFee> findById(long id) {
        return regionalBaseFeeRepository.findById(id);
    }

    /**
     * Finds all RBF entities
     * @return list of regional base fees
     */
    @Override
    public List<RegionalBaseFee> findAll() {
        return regionalBaseFeeRepository.findAll();
    }
}
