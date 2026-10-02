package com.hansol.mes.production.model.dto;

import java.time.LocalDateTime;

public record ProductionResultResponse(
        Long resultId,
        Long workOrderId,
        String orderNo,
        Long itemId,
        String itemCode,
        String itemName,
        Long plannedQty,
        Long goodQty,
        Long defectQty,
        String defectReason,
        LocalDateTime recordedAt,
        Long receiptId,
        LocalDateTime receivedAt
) {
}
