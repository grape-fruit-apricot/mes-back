package com.hansol.mes.inventory.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hansol.mes.common.dto.SearchRequest;
import com.hansol.mes.inventory.model.dto.InventoryResponse;
import com.hansol.mes.inventory.model.service.InventoryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @GetMapping
    public List<InventoryResponse> search(@Valid @ModelAttribute SearchRequest search) {
        return service.search(search);
    }

    @GetMapping("/{id}")
    public InventoryResponse find(@PathVariable("id") @Positive Long id) {
        return service.find(id);
    }
}
