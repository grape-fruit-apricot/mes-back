package com.hansol.mes.receipt.model.service;

import static com.hansol.mes.common.exception.BusinessException.requireFound;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hansol.mes.common.dto.SearchRequest;
import com.hansol.mes.receipt.model.dao.GoodsReceiptDao;
import com.hansol.mes.receipt.model.dto.GoodsReceiptResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoodsReceiptService {

    private final GoodsReceiptDao dao;

    public List<GoodsReceiptResponse> search(SearchRequest search) {
        return dao.search(search);
    }

    public GoodsReceiptResponse find(Long id) {
        return requireFound(dao.find(id), "입고 이력");
    }

    @Transactional
    public GoodsReceiptResponse receive(Long resultId) {
        dao.receive(resultId);
        return dao.findByResultId(resultId);
    }
}
