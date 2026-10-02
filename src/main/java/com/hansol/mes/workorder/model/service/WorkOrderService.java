package com.hansol.mes.workorder.model.service;

import static com.hansol.mes.common.exception.BusinessException.requireFound;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hansol.mes.common.dto.SearchRequest;
import com.hansol.mes.workorder.model.dao.WorkOrderDao;
import com.hansol.mes.workorder.model.dto.WorkOrderCreateRequest;
import com.hansol.mes.workorder.model.dto.WorkOrderResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WorkOrderService {

    private final WorkOrderDao dao;

    public List<WorkOrderResponse> search(SearchRequest search) {
        return dao.search(search);
    }

    public WorkOrderResponse find(Long id) {
        return requireFound(dao.find(id), "작업지시");
    }

    @Transactional
    public WorkOrderResponse create(WorkOrderCreateRequest request) {
        dao.insert(request);
        return dao.findByOrderNo(request.orderNo());
    }

    @Transactional
    public WorkOrderResponse start(Long id) {
        find(id);
        dao.start(id);
        return find(id);
    }
}
