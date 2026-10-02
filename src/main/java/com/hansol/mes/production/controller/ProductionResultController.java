package com.hansol.mes.production.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hansol.mes.common.dto.SearchRequest;
import com.hansol.mes.production.model.dto.ProductionResultCreateRequest;
import com.hansol.mes.production.model.dto.ProductionResultResponse;
import com.hansol.mes.production.model.service.ProductionResultService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductionResultController {

    private final ProductionResultService service;

    @GetMapping("/api/production-results")
    public List<ProductionResultResponse> search(@Valid @ModelAttribute SearchRequest search) {
        return service.search(search);
    }

    @GetMapping("/api/production-results/{id}")
    public ProductionResultResponse find(@PathVariable("id") @Positive Long id) {
        return service.find(id);
    }

    @PostMapping("/api/work-orders/{orderId}/production-result")
    public ResponseEntity<ProductionResultResponse> create(
            @PathVariable("orderId") @Positive Long orderId,
            @Valid @RequestBody ProductionResultCreateRequest request) {
        ProductionResultResponse result = service.create(orderId, request);
        return ResponseEntity
                .created(URI.create("/api/production-results/" + result.resultId()))
                .body(result);
    }
}
