package com.stock.inventorymanagement.service;

import com.stock.inventorymanagement.domain.Return;
import com.stock.inventorymanagement.dto.ReturnDTO;
import com.stock.inventorymanagement.dto.ReturnSearchCriteriaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReturnService {

    ReturnDTO createReturn(ReturnDTO returnDTO,Long userId);

    ReturnDTO getReturn(Long id);

    List<ReturnDTO> getAllReturns(Pageable pageable);

    ReturnDTO updateReturn(Long id, ReturnDTO returnDTO,Long userId);

    boolean deleteReturn(Long id,Long userId);

    public Page<ReturnDTO> searchReturns(ReturnSearchCriteriaDTO searchCriteria, Pageable pageable) ;
    public boolean checkReturnEligibility(Long orderId, Long orderItemId);


}
