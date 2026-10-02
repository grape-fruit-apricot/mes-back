package com.hansol.mes.production.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hansol.mes.common.dto.SearchRequest;
import com.hansol.mes.production.model.dto.ProductionResultCreateRequest;
import com.hansol.mes.production.model.dto.ProductionResultResponse;

@Mapper
public interface ProductionResultDao {

    List<ProductionResultResponse> search(SearchRequest search);

    ProductionResultResponse find(Long id);

    void complete(@Param("orderId") Long orderId, @Param("request") ProductionResultCreateRequest request);

    ProductionResultResponse findByWorkOrderId(Long orderId);
}
