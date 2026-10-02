package com.hansol.mes.item.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hansol.mes.common.dto.SearchRequest;
import com.hansol.mes.item.model.dto.ItemResponse;

@Mapper
public interface ItemDao {

    List<ItemResponse> search(SearchRequest search);

    ItemResponse find(Long id);
}
