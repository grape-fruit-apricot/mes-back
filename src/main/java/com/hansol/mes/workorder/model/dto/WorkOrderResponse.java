package com.hansol.mes.workorder.model.dto;

import java.time.LocalDateTime;

public record WorkOrderResponse(
        Long workOrderId,
        String orderNo,
        Long itemId,
        String itemCode,
        String itemName,
        String unit,
        Long plannedQty,
        String status,
        LocalDateTime createdAt,
        LocalDateTime completedAt,
        Long resultId,
        Long goodQty,
        Long defectQty,
        String defectReason,
        LocalDateTime recordedAt,
        Long receiptId,
        Long receivedQty,
        LocalDateTime receivedAt
) {
}
