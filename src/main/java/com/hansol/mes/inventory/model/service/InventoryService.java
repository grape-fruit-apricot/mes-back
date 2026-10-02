package com.hansol.mes.inventory.model.service;

import static com.hansol.mes.common.exception.BusinessException.requireFound;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hansol.mes.common.dto.SearchRequest;
import com.hansol.mes.inventory.model.dao.InventoryDao;
import com.hansol.mes.inventory.model.dto.InventoryResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryDao dao;

    public List<InventoryResponse> search(SearchRequest search) {
        return dao.search(search);
    }

    public InventoryResponse find(Long id) {
        return requireFound(dao.find(id), "품목 재고");
    }
}
