package com.stock.inventorymanagement.service.impl;

import com.stock.inventorymanagement.domain.Credit;
import com.stock.inventorymanagement.domain.Customer;
import com.stock.inventorymanagement.domain.Return;
import com.stock.inventorymanagement.domain.ReturnItem;
import com.stock.inventorymanagement.dto.ReturnDTO;
import com.stock.inventorymanagement.dto.ReturnItemDTO;
import com.stock.inventorymanagement.dto.ReturnSearchCriteriaDTO;
import com.stock.inventorymanagement.repository.*;
import com.stock.inventorymanagement.service.ReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReturnServiceImpl implements ReturnService {
    @Autowired
    private ReturnRepository returnRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ReturnItemRepository returnItemRepository;

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnDTO createReturn(ReturnDTO returnDTO, Long userId) {
        Return returnEntity = new Return();
        returnEntity.setOrder(orderRepository.findById(returnDTO.getOrderId()).orElse(null));
        if (returnDTO.getStatus() == null || returnDTO.getStatus().isEmpty()) {
            returnEntity.setStatus("in_progress");
        } else {
            returnEntity.setStatus(returnDTO.getStatus());
        }
        returnEntity.setReason(returnDTO.getReason());
        returnEntity.setComments(returnDTO.getComments());
        // Set date fields
        LocalDateTime now = LocalDateTime.now();
        returnEntity.setReturnDate(now); // Set current date as return date
        returnEntity.setCreditDate(now); // Set current date as credit date
        returnEntity.setCreatedAt(now); // Set current date as creation date
        returnEntity.setUpdatedAt(now);

        if (returnDTO.getReturnItems() != null) {
            BigDecimal totalCreditAmount = BigDecimal.ZERO;
            for (ReturnItemDTO returnItemDTO : returnDTO.getReturnItems()) {
                ReturnItem returnItem = new ReturnItem();
                returnItem.setReturnEntity(returnEntity);
                returnItem.setOrderItem(orderItemRepository.findById(returnItemDTO.getOrderItemId()).orElse(null));
                returnItem.setQuantityReturned(returnItemDTO.getQuantityReturned());
                returnItem.setReason(returnItemDTO.getReason());
                returnItem.setComments(returnItemDTO.getComments());
                returnItemRepository.save(returnItem);
                totalCreditAmount = totalCreditAmount.add(returnItem.getOrderItem().getPrice().multiply(BigDecimal.valueOf(returnItem.getQuantityReturned())));
            }
            returnEntity.setCreditAmount(totalCreditAmount);
        }

        returnEntity.setCreatedBy(userId);
        Return savedReturn = returnRepository.save(returnEntity);

        ReturnDTO savedReturnDTO = new ReturnDTO();
        savedReturnDTO.setId(savedReturn.getId());
        savedReturnDTO.setOrderId(savedReturn.getOrder().getId());
        savedReturnDTO.setStatus(savedReturn.getStatus());
        savedReturnDTO.setReason(savedReturn.getReason());
        savedReturnDTO.setComments(savedReturn.getComments());
        savedReturnDTO.setCreditAmount(savedReturn.getCreditAmount());
        savedReturnDTO.setReturnItems(returnDTO.getReturnItems());

        return savedReturnDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public ReturnDTO getReturn(Long id) {
        Optional<Return> optionalReturn = returnRepository.findById(id);
        if (optionalReturn.isPresent()) {
            Return returnEntity = optionalReturn.get();

            // Initialize the returnItems collection to trigger lazy loading (if applicable)
            returnEntity.getReturnItems().size();

            ReturnDTO returnDTO = new ReturnDTO();
            returnDTO.setId(returnEntity.getId());
            returnDTO.setOrderId(returnEntity.getOrder().getId());
            returnDTO.setStatus(returnEntity.getStatus());
            returnDTO.setReason(returnEntity.getReason());
            returnDTO.setComments(returnEntity.getComments());
            returnDTO.setCreditAmount(returnEntity.getCreditAmount());

            // Convert associated ReturnItem entities to DTOs and set them in the ReturnDTO
            List<ReturnItemDTO> returnItemDTOs = returnEntity.getReturnItems().stream()
                    .map(this::convertReturnItemToDTO)
                    .collect(Collectors.toList());
            returnDTO.setReturnItems(returnItemDTOs);

            return returnDTO;
        } else {
            return null;
        }
    }

    private ReturnItemDTO convertReturnItemToDTO(ReturnItem returnItem) {
        ReturnItemDTO returnItemDTO = new ReturnItemDTO();
        returnItemDTO.setId(returnItem.getId());
        returnItemDTO.setQuantityReturned(returnItem.getQuantityReturned());
        returnItemDTO.setReason(returnItem.getReason());
        returnItemDTO.setComments(returnItem.getComments());
        returnItemDTO.setOrderItemId(returnItem.getOrderItem().getId());
        // Map other fields as needed
        return returnItemDTO;
    }



    @Override
    @Transactional(readOnly = true)
    public List<ReturnDTO> getAllReturns(Pageable pageable) {
        Page<Return> returnPage = returnRepository.findAll(pageable);

        List<ReturnDTO> returnDTOs = new ArrayList<>();
        for (Return returnEntity : returnPage.getContent()) {
            // Initialize the returnItems collection to trigger lazy loading (if applicable)
            returnEntity.getReturnItems().size();

            ReturnDTO returnDTO = new ReturnDTO();
            returnDTO.setId(returnEntity.getId());
            returnDTO.setOrderId(returnEntity.getOrder().getId());
            returnDTO.setStatus(returnEntity.getStatus());
            returnDTO.setReason(returnEntity.getReason());
            returnDTO.setComments(returnEntity.getComments());
            returnDTO.setCreditAmount(returnEntity.getCreditAmount());

            // Convert associated ReturnItem entities to DTOs and set them in the ReturnDTO
            List<ReturnItemDTO> returnItemDTOs = returnEntity.getReturnItems().stream()
                    .map(this::convertReturnItemToDTO)
                    .collect(Collectors.toList());
            returnDTO.setReturnItems(returnItemDTOs);

            returnDTOs.add(returnDTO);
        }

        return returnDTOs;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnDTO updateReturn(Long id, ReturnDTO returnDTO, Long userId) {
        Optional<Return> optionalReturn = returnRepository.findById(id);
        if (optionalReturn.isPresent()) {
            Return returnEntity = optionalReturn.get();

            // Update return status, reason, and comments
            if (returnDTO.getStatus() != null && !returnDTO.getStatus().isEmpty()) {
                returnEntity.setStatus(returnDTO.getStatus());
            }
            returnEntity.setReason(returnDTO.getReason());
            returnEntity.setComments(returnDTO.getComments());
            LocalDateTime now = LocalDateTime.now();

            returnEntity.setUpdatedAt(now);

            // Update return items and calculate total credit amount
            BigDecimal totalCreditAmount = BigDecimal.ZERO;
            if (returnDTO.getReturnItems() != null) {
                for (ReturnItemDTO returnItemDTO : returnDTO.getReturnItems()) {
                    ReturnItem returnItem = null;
                    // Check if the return item already exists
                    for (ReturnItem existingItem : returnEntity.getReturnItems()) {
                        if (existingItem.getOrderItem().getId().equals(returnItemDTO.getOrderItemId())) {
                            returnItem = existingItem;
                            break;
                        }
                    }
                    // If return item does not exist, create a new one
                    if (returnItem == null) {
                        returnItem = new ReturnItem();
                        returnItem.setReturnEntity(returnEntity);
                    }
                    returnItem.setOrderItem(orderItemRepository.findById(returnItemDTO.getOrderItemId()).orElse(null));
                    returnItem.setQuantityReturned(returnItemDTO.getQuantityReturned());
                    returnItem.setReason(returnItemDTO.getReason());
                    returnItem.setComments(returnItemDTO.getComments());
                    returnItemRepository.save(returnItem);

                    // Calculate total credit amount
                    totalCreditAmount = totalCreditAmount.add(returnItem.getOrderItem().getPrice().multiply(BigDecimal.valueOf(returnItem.getQuantityReturned())));
                }
            }
            returnEntity.setCreditAmount(totalCreditAmount);

            // If return status is "approved", create a credit entity
            if ("approved".equalsIgnoreCase(returnDTO.getStatus())) {
                Long userIdFromOrder = returnEntity.getOrder().getUser().getId();
                Optional<Customer> optionalCustomer = customerRepository.findByUserId(userIdFromOrder);
                if (optionalCustomer.isPresent()) {
                    Customer customer = optionalCustomer.get();
                    Credit credit = new Credit();
                    credit.setCustomer(customer);
                    credit.setAmount(returnEntity.getCreditAmount());
                    credit.setType("RETURN");
                    credit.setStatus("ACTIVE");

                    // Convert LocalDateTime to Date
                    LocalDateTime issueDateTime = LocalDateTime.now();
                    Instant instant = issueDateTime.atZone(ZoneId.systemDefault()).toInstant();
                    Date issueDate = Date.from(instant);

                    credit.setIssueDate(issueDate); // Set issue date as java.util.Date
                    credit.setExpiryDate(Date.from(issueDateTime.plusYears(1).atZone(ZoneId.systemDefault()).toInstant())); // Set expiry date to one year from issue date
                    creditRepository.save(credit);
                }
            }

            // Save return entity
            returnEntity.setCreatedBy(userId);
            Return savedReturn = returnRepository.save(returnEntity);

            // Convert saved return entity to DTO
            ReturnDTO savedReturnDTO = convertToDTO(savedReturn);

            return savedReturnDTO;
        }
        return null;
    }



    @Override
    public boolean deleteReturn(Long id, Long userId) {
        if (returnRepository.existsById(id)) {
            returnRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ReturnDTO convertToDTO(Return returnEntity) {
        ReturnDTO returnDTO = new ReturnDTO();
        returnDTO.setId(returnEntity.getId());
        returnDTO.setOrderId(returnEntity.getOrder().getId());
        returnDTO.setStatus(returnEntity.getStatus());
        returnDTO.setReason(returnEntity.getReason());
        returnDTO.setComments(returnEntity.getComments());
        returnDTO.setCreditAmount(returnEntity.getCreditAmount());

        // Convert associated ReturnItem entities to DTOs and set them in the ReturnDTO
        List<ReturnItemDTO> returnItemDTOs = returnEntity.getReturnItems().stream()
                .map(this::convertReturnItemToDTO)
                .collect(Collectors.toList());
        returnDTO.setReturnItems(returnItemDTOs);
        return returnDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReturnDTO> searchReturns(ReturnSearchCriteriaDTO searchCriteria, Pageable pageable) {
        Specification<Return> spec = createSpecification(searchCriteria);
        Page<Return> returnPage = returnRepository.findAll(spec, pageable);
        List<ReturnDTO> returnDTOs = returnPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(returnDTOs, pageable, returnPage.getTotalElements());
    }


    private Specification<Return> createSpecification(ReturnSearchCriteriaDTO searchCriteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            switch (searchCriteria.getField()) {
                case "approvalStatus":
                    Path<String> statusPath = root.get("status");
                    predicates.add(cb.equal(statusPath, searchCriteria.getValue()));
                    break;
                case "reason":
                    Path<String> reasonPath = root.get("reason");
                    predicates.add(cb.equal(reasonPath, searchCriteria.getValue()));
                    break;
                case "returnDate":
                    Path<LocalDateTime> returnDatePath = root.get("returnDate");
                    predicates.add(cb.equal(returnDatePath, LocalDateTime.parse(searchCriteria.getValue())));
                    break;
                default:
                    break;
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}