package com.hansol.mes.workorder.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hansol.mes.common.dto.SearchRequest;
import com.hansol.mes.workorder.model.dto.WorkOrderCreateRequest;
import com.hansol.mes.workorder.model.dto.WorkOrderResponse;
import com.hansol.mes.workorder.model.service.WorkOrderService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/work-orders")
@RequiredArgsConstructor
public class WorkOrderController {

    private final WorkOrderService service;

    @GetMapping
    public List<WorkOrderResponse> search(@Valid @ModelAttribute SearchRequest search) {
        return service.search(search);
    }

    @GetMapping("/{id}")
    public WorkOrderResponse find(@PathVariable("id") @Positive Long id) {
        return service.find(id);
    }

    @PostMapping
    public ResponseEntity<WorkOrderResponse> create(
            @Valid @RequestBody WorkOrderCreateRequest request
    ) {
        WorkOrderResponse result = service.create(request);
        return ResponseEntity
                .created(URI.create("/api/work-orders/" + result.workOrderId()))
                .body(result);
    }

    @PostMapping("/{id}/start")
    public WorkOrderResponse start(@PathVariable("id") @Positive Long id) {
        return service.start(id);
    }
}
