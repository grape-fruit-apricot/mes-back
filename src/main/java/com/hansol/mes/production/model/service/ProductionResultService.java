package com.hansol.mes.production.model.service;

import static com.hansol.mes.common.exception.BusinessException.requireFound;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hansol.mes.common.dto.SearchRequest;
import com.hansol.mes.production.model.dao.ProductionResultDao;
import com.hansol.mes.production.model.dto.ProductionResultCreateRequest;
import com.hansol.mes.production.model.dto.ProductionResultResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductionResultService {

    private final ProductionResultDao dao;

    public List<ProductionResultResponse> search(SearchRequest search) {
        return dao.search(search);
    }

    public ProductionResultResponse find(Long id) {
        return requireFound(dao.find(id), "생산실적");
    }

    @Transactional
    public ProductionResultResponse create(Long orderId, ProductionResultCreateRequest request) {
        dao.complete(orderId, request);
        return dao.findByWorkOrderId(orderId);
    }
}
