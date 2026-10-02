package com.hansol.mes.item.model.service;

import static com.hansol.mes.common.exception.BusinessException.requireFound;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hansol.mes.common.dto.SearchRequest;
import com.hansol.mes.item.model.dao.ItemDao;
import com.hansol.mes.item.model.dto.ItemResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemDao dao;

    public List<ItemResponse> search(SearchRequest search) {
        return dao.search(search);
    }

    public ItemResponse find(Long id) {
        return requireFound(dao.find(id), "품목");
    }
}
