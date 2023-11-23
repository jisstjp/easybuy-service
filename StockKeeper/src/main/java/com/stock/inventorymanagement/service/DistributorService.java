package com.stock.inventorymanagement.service;

import com.stock.inventorymanagement.domain.Distributor;
import com.stock.inventorymanagement.dto.DistributorDto;

import java.util.List;
import java.util.Optional;

public interface DistributorService {

    DistributorDto createDistributor(DistributorDto distributor);

    DistributorDto getDistributorById(Long id);

    List<DistributorDto> getAllDistributors();

    DistributorDto updateDistributor(Long id, DistributorDto distributorDetails);

    void deleteDistributor(Long id);
}
