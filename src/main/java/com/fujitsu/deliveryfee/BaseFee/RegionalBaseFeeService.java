package com.fujitsu.deliveryfee.BaseFee;

import java.util.List;
import java.util.Optional;

public interface RegionalBaseFeeService {

    void save(RegionalBaseFee regionalBaseFee);
    Optional<RegionalBaseFee> findById(long id);
    List<RegionalBaseFee> findAll();

}
