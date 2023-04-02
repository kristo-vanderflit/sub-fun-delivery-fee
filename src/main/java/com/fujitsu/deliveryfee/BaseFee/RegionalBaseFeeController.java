package com.fujitsu.deliveryfee.BaseFee;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/basefees")
@Tag(name = "Regional Base Fee")
public class RegionalBaseFeeController {
    private final RegionalBaseFeeService regionalBaseFeeService;
    public RegionalBaseFeeController(RegionalBaseFeeService regionalBaseFeeService) {
        this.regionalBaseFeeService = regionalBaseFeeService;
    }


    /**
     * Finds Regional Base Fee by id
     * @param id fee id
     * @return fee
     */
    @GetMapping("{id}")
    @Operation(summary = "Get fee by ID")
    public ResponseEntity<Optional<RegionalBaseFee>> findFeeById(@PathVariable Long id) {

        if (regionalBaseFeeService.findById(id).isPresent()) {
            return new ResponseEntity<>(regionalBaseFeeService.findById(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Finds all fees
     * @return list of fees
     */
    @GetMapping
    @Operation(summary = "Getting list of fees")
    public ResponseEntity<List<RegionalBaseFee>> findAllFees() {
        return new ResponseEntity<>(regionalBaseFeeService.findAll(), HttpStatus.OK);
    }

    /**
     * Updates Regional Base Fee values by fee id
     * @param id fee id
     * @param value fee values
     * @return fee entity
     */
    @PutMapping("{id}/{value}")
    @Operation(summary = "Updates fee by ID and value",
            description = """
                    {value} variable should get 3 values.\s
                    Use commas to separate values.\s
                    example: 2,2,2 -> OK.\s
                    example: 2.0,2,2.9 -> OK.\s
                    example: 222 -> NOT OK.""")

    public ResponseEntity<RegionalBaseFee> updateFee(@PathVariable Long id, @PathVariable String value) {

        String[] split = value.split(",");
        if (split.length == 3 && regionalBaseFeeService.findById(id).isPresent()) {
                RegionalBaseFee regionalBaseFee = regionalBaseFeeService.findById(id).get();

                try {
                    regionalBaseFee.setBike(Double.parseDouble(split[0]));
                    regionalBaseFee.setScooter(Double.parseDouble(split[1]));
                    regionalBaseFee.setCar(Double.parseDouble(split[2]));
                    regionalBaseFeeService.save(regionalBaseFee);
                    return new ResponseEntity<>(regionalBaseFee, HttpStatus.OK);
                } catch (NumberFormatException e) {
                    throw new NumberFormatException();
                }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
