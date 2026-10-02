package com.hansol.mes.production.model.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductionResultCreateRequest(
        @NotNull
        @Min(0)
        @Max(9999999999L)
        Long goodQty,

        @NotNull
        @Min(0)
        @Max(9999999999L)
        Long defectQty,

        @Size(max = 200)
        String defectReason
) {
    @AssertTrue(message = "불량 수량이 있으면 불량 사유를 입력해 주세요.")
    public boolean isDefectReasonValid() {
        return defectQty == null || defectQty == 0 || (defectReason != null && !defectReason.isBlank());
    }
}
