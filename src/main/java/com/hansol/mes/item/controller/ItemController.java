package com.hansol.mes.item.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hansol.mes.common.dto.SearchRequest;
import com.hansol.mes.item.model.dto.ItemResponse;
import com.hansol.mes.item.model.service.ItemService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;

    @GetMapping
    public List<ItemResponse> search(@Valid @ModelAttribute SearchRequest search) {
        return service.search(search);
    }

    @GetMapping("/{id}")
    public ItemResponse find(@PathVariable("id") @Positive Long id) {
        return service.find(id);
    }
}
