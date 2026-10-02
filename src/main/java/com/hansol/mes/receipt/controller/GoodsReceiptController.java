package com.hansol.mes.receipt.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hansol.mes.common.dto.SearchRequest;
import com.hansol.mes.receipt.model.dto.GoodsReceiptResponse;
import com.hansol.mes.receipt.model.service.GoodsReceiptService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GoodsReceiptController {

    private final GoodsReceiptService service;

    @GetMapping("/api/goods-receipts")
    public List<GoodsReceiptResponse> search(@Valid @ModelAttribute SearchRequest search) {
        return service.search(search);
    }

    @GetMapping("/api/goods-receipts/{id}")
    public GoodsReceiptResponse find(@PathVariable("id") @Positive Long id) {
        return service.find(id);
    }

    @PostMapping("/api/production-results/{resultId}/receipt")
    public ResponseEntity<GoodsReceiptResponse> receive(
            @PathVariable("resultId") @Positive Long resultId
    ) {
        GoodsReceiptResponse result = service.receive(resultId);
        return ResponseEntity
                .created(URI.create("/api/goods-receipts/" + result.receiptId()))
                .body(result);
    }
}
