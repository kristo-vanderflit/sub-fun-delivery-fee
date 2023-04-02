package com.fujitsu.deliveryfee.ExtraFee;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/extrafees")
@Tag(name = "Extra Fee")
public class ExtraFeeController {

    private final ExtraFeeService extraFeeService;
    public ExtraFeeController(ExtraFeeService extraFeeService) {
        this.extraFeeService = extraFeeService;
    }

    /**
     * Finds all extra fees
     * @return list of extra fees
     */
    @GetMapping
    @Operation(summary = "Getting list of fees")
    public ResponseEntity<List<ExtraFee>> findAllFees() {
        return new ResponseEntity<>(extraFeeService.findAllFees(), HttpStatus.OK);
    }

    /**
     * Finds extra fee entity by id
     * @param id fee id
     * @return fee entity
     */
    @GetMapping("{id}")
    @Operation(summary = "Get fee by ID")
    public Optional<ExtraFee> findFee(@PathVariable Long id) {
        return extraFeeService.findFee(id);
    }

    /**
     * Updates the extra fee values
     * @param id extra fee id
     * @param value values
     * value example: 2,2,2,2,2 -> OK, separated by commas
     */
    @PutMapping("{id}/{value}")
    @Operation(summary = "Updates fee by ID and value",
            description = """
                    {value} variable should get 5 values.\s
                    Use commas to separate values.\s
                    example: 2,2,2,2,2 -> OK.\s
                    example: 2.0,2,2.9,3443.5,123 -> OK.\s
                    example: 12345 -> NOT OK.""")
    public void setValues(@PathVariable Long id, @PathVariable String value) {
        extraFeeService.setExtraFeeValues(id, value);
    }
}
