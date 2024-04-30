package com.stock.inventorymanagement.service.impl;

import com.stock.inventorymanagement.domain.Role;
import com.stock.inventorymanagement.domain.SalesPerson;
import com.stock.inventorymanagement.domain.User;
import com.stock.inventorymanagement.dto.SalesPersonDTO;
import com.stock.inventorymanagement.repository.RoleRepository;
import com.stock.inventorymanagement.repository.SalesPersonRepository;
import com.stock.inventorymanagement.repository.UserRepository;
import com.stock.inventorymanagement.service.SalesPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SalesPersonServiceImpl implements SalesPersonService {

    private final SalesPersonRepository salesPersonRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    public SalesPersonServiceImpl(SalesPersonRepository salesPersonRepository) {
        this.salesPersonRepository = salesPersonRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public SalesPersonDTO saveSalesPerson(SalesPersonDTO salesPersonDto) {
        SalesPerson salesPerson = convertToEntity(salesPersonDto);
        SalesPerson savedSalesPerson = salesPersonRepository.save(salesPerson);
        createUserForSalesPerson(salesPersonDto);
        return convertToDto(savedSalesPerson);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public SalesPersonDTO updateSalesPerson(Long id, SalesPersonDTO salesPersonDto) {
        Optional<SalesPerson> existingSalesPerson = salesPersonRepository.findById(id);
        if (existingSalesPerson.isPresent()) {
            SalesPerson updatedSalesPerson = existingSalesPerson.get();
            updateEntityWithDto(updatedSalesPerson, salesPersonDto);
            SalesPerson savedSalesPerson = salesPersonRepository.save(updatedSalesPerson);
            return convertToDto(savedSalesPerson);
        } else {
            throw new RuntimeException("SalesPerson not found with id: " + id);
        }
    }

    @Override
    public Optional<SalesPersonDTO> findSalesPersonById(Long id) {
        return salesPersonRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public Optional<SalesPersonDTO> findSalesPersonByEmail(String email) {
        return salesPersonRepository.findByEmail(email).map(this::convertToDto);
    }

    @Override
    public List<SalesPersonDTO> findAllSalesPersons() {
        return salesPersonRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSalesPersonById(Long id) {
        salesPersonRepository.deleteById(id);
    }

    private SalesPersonDTO convertToDto(SalesPerson salesPerson) {
        SalesPersonDTO dto = new SalesPersonDTO();
        dto.setId(salesPerson.getId());
        dto.setFirstName(salesPerson.getFirstName());
        dto.setLastName(salesPerson.getLastName());
        dto.setEmail(salesPerson.getEmail());
        dto.setPhone(salesPerson.getPhone());
        dto.setDepartment(salesPerson.getDepartment());
        dto.setPosition(salesPerson.getPosition());
        //dto.setHireDate(salesPerson.getHireDate());
        dto.setTerritory(salesPerson.getTerritory());
        dto.setSalesQuota(salesPerson.getSalesQuota());
        dto.setSalesYTD(salesPerson.getSalesYTD());
       // dto.setIsActive(salesPerson.getIsActive());
       // dto.setLastLogin(salesPerson.getLastLogin());
        // Set other fields as needed
        return dto;
    }

    private SalesPerson convertToEntity(SalesPersonDTO salesPersonDto) {
        SalesPerson salesPerson = new SalesPerson();
        if (salesPersonDto.getId() != null) { // Useful for updates
            salesPerson.setId(salesPersonDto.getId());
        }
        salesPerson.setFirstName(salesPersonDto.getFirstName());
        salesPerson.setLastName(salesPersonDto.getLastName());
        salesPerson.setEmail(salesPersonDto.getEmail());
        salesPerson.setPhone(salesPersonDto.getPhone());
        salesPerson.setDepartment(salesPersonDto.getDepartment());
        salesPerson.setPosition(salesPersonDto.getPosition());
        //salesPerson.setHireDate(salesPersonDto.getHireDate());
        salesPerson.setTerritory(salesPersonDto.getTerritory());
        salesPerson.setSalesQuota(salesPersonDto.getSalesQuota());
        salesPerson.setSalesYTD(salesPersonDto.getSalesYTD());
       // salesPerson.setIsActive(salesPersonDto.getIsActive());
       // salesPerson.setLastLogin(salesPersonDto.getLastLogin());
        // Map other fields as needed
        return salesPerson;
    }

    private void updateEntityWithDto(SalesPerson salesPerson, SalesPersonDTO salesPersonDto) {
        // Update only the fields that can be changed
        salesPerson.setFirstName(salesPersonDto.getFirstName());
        salesPerson.setLastName(salesPersonDto.getLastName());
        salesPerson.setEmail(salesPersonDto.getEmail());
        salesPerson.setPhone(salesPersonDto.getPhone());
        salesPerson.setDepartment(salesPersonDto.getDepartment());
        salesPerson.setPosition(salesPersonDto.getPosition());
       // salesPerson.setHireDate(salesPersonDto.getHireDate());
        salesPerson.setTerritory(salesPersonDto.getTerritory());
        salesPerson.setSalesQuota(salesPersonDto.getSalesQuota());
        salesPerson.setSalesYTD(salesPersonDto.getSalesYTD());
        //salesPerson.setIsActive(salesPersonDto.getIsActive());
        //salesPerson.setLastLogin(salesPersonDto.getLastLogin());
        // Update other fields as necessary
    }


    private void createUserForSalesPerson(SalesPersonDTO salesPerson) {
        User newUser = new User();
        newUser.setUsername(salesPerson.getUserName());
        newUser.setPassword(salesPerson.getPassword());
        newUser.setEmail(salesPerson.getEmail());
        newUser.setFirstName(salesPerson.getFirstName());
        newUser.setLastName(salesPerson.getLastName());

        Role role = roleRepository.findByName("SALES_PERSON");
        if (role != null) {
            newUser.getRoles().add(role);
        } else {
           // log.warn("Role SALES_PERSON not found and is required for creating a user.");
            // Optionally throw an exception or handle this case as required
        }

        // Save the new user to the user repository
        userRepository.save(newUser);

        // Optionally, perform other post-user-creation actions (like sending email notifications)
    }
}
