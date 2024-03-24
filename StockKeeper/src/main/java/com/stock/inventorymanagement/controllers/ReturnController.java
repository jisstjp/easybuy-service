package com.stock.inventorymanagement.controllers;

import com.stock.inventorymanagement.dto.ReturnDTO;
import com.stock.inventorymanagement.dto.ReturnSearchCriteriaDTO;
import com.stock.inventorymanagement.service.ReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/returns")
public class ReturnController extends BaseController {

    @Autowired
    private ReturnService returnService;

    @PostMapping
    public ResponseEntity<ReturnDTO> initiateReturn(HttpServletRequest request, @RequestBody ReturnDTO returnDTO) {
        Long userId = getUserIdFromToken(request);
        ReturnDTO createdReturn = returnService.createReturn(returnDTO,userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReturn);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnDTO> getReturn(@PathVariable Long id) {
        ReturnDTO returnDTO = returnService.getReturn(id);
        if (returnDTO != null) {
            return ResponseEntity.ok(returnDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ReturnDTO>> getAllReturns(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        List<ReturnDTO> returnList = returnService.getAllReturns(pageable);
        return ResponseEntity.ok(returnList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReturnDTO> updateReturn(HttpServletRequest request,@PathVariable Long id, @RequestBody ReturnDTO returnDTO) {
        Long userId = getUserIdFromToken(request);
        ReturnDTO updatedReturn = returnService.updateReturn(id, returnDTO,userId);
        if (updatedReturn != null) {
            return ResponseEntity.ok(updatedReturn);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReturn(HttpServletRequest request,@PathVariable Long id) {
        Long userId = getUserIdFromToken(request);
        boolean deleted = returnService.deleteReturn(id,userId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/search")
    public ResponseEntity<Page<ReturnDTO>> searchReturns(@RequestBody ReturnSearchCriteriaDTO searchCriteria, Pageable pageable) {
        Page<ReturnDTO> resultPage = returnService.searchReturns(searchCriteria, pageable);
        return ResponseEntity.ok(resultPage);
    }

    @GetMapping("/checkEligibility")
    public ResponseEntity<Boolean> checkReturnEligibility(
            @RequestParam("orderId") Long orderId,
            @RequestParam("orderItemId") Long orderItemId) {

        // Check the eligibility for return using the ReturnService
        boolean isEligible = returnService.checkReturnEligibility(orderId, orderItemId);

        // Return the result as a ResponseEntity
        return ResponseEntity.ok(isEligible);
    }

}