package com.hansol.mes;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hansol.mes.common.exception.GlobalExceptionHandler;
import com.hansol.mes.production.controller.ProductionResultController;
import com.hansol.mes.production.model.dao.ProductionResultDao;
import com.hansol.mes.production.model.service.ProductionResultService;
import com.hansol.mes.receipt.controller.GoodsReceiptController;
import com.hansol.mes.receipt.model.dao.GoodsReceiptDao;
import com.hansol.mes.receipt.model.service.GoodsReceiptService;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class ApiValidationTest {
    private ProductionResultDao production;
    private GoodsReceiptDao receipts;
    private MockMvc mvc;

    @BeforeEach
    void setup() {
        production = mock(ProductionResultDao.class);
        receipts = mock(GoodsReceiptDao.class);
        mvc = standaloneSetup(new ProductionResultController(new ProductionResultService(production)),
                new GoodsReceiptController(new GoodsReceiptService(receipts)))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(
                        new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT)))
                .build();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "{\"goodQty\":-1,\"defectQty\":0}",
        "{\"goodQty\":1.5,\"defectQty\":0}",
        "{\"goodQty\":null,\"defectQty\":0}",
        "{\"goodQty\":97,\"defectQty\":3,\"defectReason\":\" \"}",
        "{\"goodQty\":10000000000,\"defectQty\":0}"
    })
    void invalidQuantitiesNeverReachDatabase(String body) throws Exception {
        mvc.perform(post("/api/work-orders/1/production-result").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value("INVALID_INPUT"));
        verifyNoInteractions(production);
    }

    @ParameterizedTest
    @ValueSource(strings = {"?page=0", "?size=101", "?from=2026-09-30&to=2026-09-01", "?status=UNKNOWN"})
    void invalidSearchNeverReachesDatabase(String query) throws Exception {
        mvc.perform(get("/api/production-results" + query)).andExpect(status().isBadRequest());
        verifyNoInteractions(production);
    }

    @Test
    void missingResultReturns404() throws Exception {
        mvc.perform(get("/api/production-results/99")).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    @Test
    void quantityMismatchReturnsUsefulError() throws Exception {
        doThrow(new DataIntegrityViolationException("internal SQL", new SQLException("internal", "", 20003)))
                .when(production).complete(eq(1L), any());
        mvc.perform(post("/api/work-orders/1/production-result").contentType(MediaType.APPLICATION_JSON)
                .content("{\"goodQty\":90,\"defectQty\":3,\"defectReason\":\"외관 불량\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("PRODUCTION_QUANTITY_MISMATCH"));
    }

    @Test
    void duplicateReceiptReturns409() throws Exception {
        doThrow(new DataIntegrityViolationException("internal SQL", new SQLException("internal", "", 20005)))
                .when(receipts).receive(1L);
        mvc.perform(post("/api/production-results/1/receipt"))
                .andExpect(status().isConflict()).andExpect(jsonPath("$.code").value("ALREADY_RECEIVED"));
    }
}

