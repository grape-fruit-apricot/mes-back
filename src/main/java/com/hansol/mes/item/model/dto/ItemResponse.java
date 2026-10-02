package com.hansol.mes.item.model.dto;

public record ItemResponse(
        Long itemId,
        String itemCode,
        String itemName,
        String unit
) {
}
