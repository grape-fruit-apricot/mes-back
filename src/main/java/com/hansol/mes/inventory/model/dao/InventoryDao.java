package com.hansol.mes.inventory.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hansol.mes.common.dto.SearchRequest;
import com.hansol.mes.inventory.model.dto.InventoryResponse;

@Mapper
public interface InventoryDao {

    List<InventoryResponse> search(SearchRequest search);

    InventoryResponse find(Long id);
}
