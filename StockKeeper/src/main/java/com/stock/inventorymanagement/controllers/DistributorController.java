package com.stock.inventorymanagement.controllers;
import com.stock.inventorymanagement.dto.DistributorDto;
import com.stock.inventorymanagement.service.DistributorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/distributors")
public class DistributorController {

    private final DistributorService distributorService;

    @Autowired
    public DistributorController(DistributorService distributorService) {
        this.distributorService = distributorService;
    }

    @PostMapping
    public ResponseEntity<DistributorDto> createDistributor(@RequestBody DistributorDto distributorDto) {
        DistributorDto savedDistributor = distributorService.createDistributor(distributorDto);
        return ResponseEntity.ok(savedDistributor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DistributorDto> getDistributorById(@PathVariable Long id) {
        DistributorDto distributor = distributorService.getDistributorById(id);
        return ResponseEntity.ok(distributor);
    }

    @GetMapping
    public ResponseEntity<List<DistributorDto>> getAllDistributors() {
        List<DistributorDto> distributors = distributorService.getAllDistributors();
        return ResponseEntity.ok(distributors);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DistributorDto> updateDistributor(@PathVariable Long id, @RequestBody DistributorDto distributorDto) {
        DistributorDto updatedDistributor = distributorService.updateDistributor(id, distributorDto);
        return ResponseEntity.ok(updatedDistributor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDistributor(@PathVariable Long id) {
        distributorService.deleteDistributor(id);
        return ResponseEntity.ok().build();
    }
}
