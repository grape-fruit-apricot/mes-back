package com.hansol.mes;

import com.hansol.mes.receipt.model.dao.GoodsReceiptDao;
import com.hansol.mes.receipt.model.service.GoodsReceiptService;
import java.sql.Connection;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransactionBoundaryTest {
    @Configuration
    @EnableTransactionManagement
    static class TransactionConfig {}

    @Test
    void receiptFailureRollsBackInsteadOfCommitting() throws Exception {
        Connection connection = mock(Connection.class);
        when(connection.getAutoCommit()).thenReturn(true);
        DataSource source = mock(DataSource.class);
        when(source.getConnection()).thenReturn(connection);
        GoodsReceiptDao dao = mock(GoodsReceiptDao.class);
        doThrow(new IllegalStateException("simulated failure")).when(dao).receive(1L);
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(TransactionConfig.class);
            context.registerBean(DataSourceTransactionManager.class, () -> new DataSourceTransactionManager(source));
            context.registerBean(GoodsReceiptDao.class, () -> dao);
            context.registerBean(GoodsReceiptService.class);
            context.refresh();
            assertThatThrownBy(() -> context.getBean(GoodsReceiptService.class).receive(1L))
                    .isInstanceOf(IllegalStateException.class);
            verify(connection).rollback();
            verify(connection, never()).commit();
        }
    }
}

