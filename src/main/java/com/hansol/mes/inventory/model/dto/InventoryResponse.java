package com.hansol.mes.inventory.model.dto;

import java.time.LocalDateTime;

public record InventoryResponse(
        Long itemId,
        String itemCode,
        String itemName,
        String unit,
        Long quantity,
        LocalDateTime updatedAt
) {
}
