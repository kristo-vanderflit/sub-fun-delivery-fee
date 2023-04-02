package com.fujitsu.deliveryfee.ExtraFee;

import java.util.List;
import java.util.Optional;

public interface ExtraFeeService {

    List<ExtraFee> findAllFees();
    Optional<ExtraFee> findFee(Long id);
    void setExtraFeeValues(Long id, String value);

}
