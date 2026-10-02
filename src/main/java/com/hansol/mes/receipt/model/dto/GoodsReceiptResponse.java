package com.hansol.mes.receipt.model.dto;

import java.time.LocalDateTime;

public record GoodsReceiptResponse(
        Long receiptId,
        Long resultId,
        Long workOrderId,
        String orderNo,
        Long itemId,
        String itemCode,
        String itemName,
        Long receivedQty,
        LocalDateTime receivedAt
) {
}
