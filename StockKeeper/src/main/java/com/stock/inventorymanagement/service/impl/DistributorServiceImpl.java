package com.stock.inventorymanagement.service.impl;

import com.stock.inventorymanagement.domain.Distributor;
import com.stock.inventorymanagement.dto.DistributorDto;
import com.stock.inventorymanagement.mapper.DistributorMapper;
import com.stock.inventorymanagement.repository.DistributorRepository;
import com.stock.inventorymanagement.service.DistributorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DistributorServiceImpl implements DistributorService {

    private final DistributorRepository distributorRepository;
    private final DistributorMapper distributorMapper;

    @Autowired
    public DistributorServiceImpl(DistributorRepository distributorRepository, DistributorMapper distributorMapper) {
        this.distributorRepository = distributorRepository;
        this.distributorMapper = distributorMapper;
    }

    @Override
    public DistributorDto createDistributor(DistributorDto distributorDto) {
        Distributor distributor = distributorMapper.toEntity(distributorDto);
        Distributor savedDistributor = distributorRepository.save(distributor);
        return distributorMapper.toDto(savedDistributor);
    }

    @Override
    public DistributorDto getDistributorById(Long id) {
        Distributor distributor = distributorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Distributor not found for this id :: " + id));
        return distributorMapper.toDto(distributor);
    }

    @Override
    public List<DistributorDto> getAllDistributors() {
        List<Distributor> distributors = distributorRepository.findAll();
        return distributors.stream()
                .map(distributorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DistributorDto updateDistributor(Long id, DistributorDto distributorDto) {
        Distributor existingDistributor = distributorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Distributor not found for this id :: " + id));
        distributorMapper.updateEntityFromDto(distributorDto, existingDistributor);
        Distributor updatedDistributor = distributorRepository.save(existingDistributor);
        return distributorMapper.toDto(updatedDistributor);
    }

    @Override
    public void deleteDistributor(Long id) {
        distributorRepository.deleteById(id);
    }
}
