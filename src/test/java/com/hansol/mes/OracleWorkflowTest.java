package com.hansol.mes;

import com.hansol.mes.inventory.model.service.InventoryService;
import com.hansol.mes.production.model.dto.ProductionResultCreateRequest;
import com.hansol.mes.production.model.dto.ProductionResultResponse;
import com.hansol.mes.production.model.service.ProductionResultService;
import com.hansol.mes.receipt.model.dto.GoodsReceiptResponse;
import com.hansol.mes.receipt.model.service.GoodsReceiptService;
import com.hansol.mes.workorder.model.dto.WorkOrderCreateRequest;
import com.hansol.mes.workorder.model.dto.WorkOrderResponse;
import com.hansol.mes.workorder.model.service.WorkOrderService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@EnabledIfEnvironmentVariable(named = "MES_ORACLE_TEST", matches = "true")
class OracleWorkflowTest {
    @Autowired JdbcTemplate jdbc;
    @Autowired WorkOrderService orders;
    @Autowired ProductionResultService production;
    @Autowired GoodsReceiptService receipts;
    @Autowired InventoryService inventory;

    @Test
    void orderProductionReceiptAndInventoryStayConnected() {
        String code = "TEST-" + UUID.randomUUID().toString().substring(0, 20);
        jdbc.update("INSERT INTO item(item_code, item_name) VALUES (?, ?)", code, "통합 테스트 품목");
        Long itemId = jdbc.queryForObject("SELECT item_id FROM item WHERE item_code = ?", Long.class, code);
        WorkOrderResponse order = orders.create(new WorkOrderCreateRequest(code, itemId, 100L));
        assertThat(order.status()).isEqualTo("READY");
        assertThat(orders.start(order.workOrderId()).status()).isEqualTo("IN_PROGRESS");
        ProductionResultResponse result = production.create(order.workOrderId(), new ProductionResultCreateRequest(97L, 3L, "외관 불량"));
        assertThat(orders.find(order.workOrderId()).status()).isEqualTo("COMPLETED");
        assertThat(inventory.find(itemId).quantity()).isZero();
        GoodsReceiptResponse receipt = receipts.receive(result.resultId());
        assertThat(receipt.receivedQty()).isEqualTo(97);
        assertThat(inventory.find(itemId).quantity()).isEqualTo(97);
        assertThat(orders.find(order.workOrderId()).receiptId()).isEqualTo(receipt.receiptId());
    }
}

