package com.hansol.mes.receipt.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hansol.mes.common.dto.SearchRequest;
import com.hansol.mes.receipt.model.dto.GoodsReceiptResponse;

@Mapper
public interface GoodsReceiptDao {

    List<GoodsReceiptResponse> search(SearchRequest search);

    GoodsReceiptResponse find(Long id);

    void receive(Long resultId);

    GoodsReceiptResponse findByResultId(Long resultId);
}
