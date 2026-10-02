package com.hansol.mes.workorder.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hansol.mes.common.dto.SearchRequest;
import com.hansol.mes.workorder.model.dto.WorkOrderCreateRequest;
import com.hansol.mes.workorder.model.dto.WorkOrderResponse;

@Mapper
public interface WorkOrderDao {

    List<WorkOrderResponse> search(SearchRequest search);

    WorkOrderResponse find(Long id);

    int insert(WorkOrderCreateRequest request);

    WorkOrderResponse findByOrderNo(String orderNo);

    void start(Long id);
}
