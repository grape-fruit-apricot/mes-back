package com.hansol.mes.workorder.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record WorkOrderCreateRequest(
        @NotBlank
        @Size(max = 30)
        String orderNo,

        @NotNull
        @Positive
        Long itemId,

        @NotNull
        @Positive
        @Max(9999999999L)
        Long plannedQty
) {
}
