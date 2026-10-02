package com.hansol.mes;

import com.hansol.mes.common.dto.SearchRequest;
import com.hansol.mes.production.model.dto.ProductionResultCreateRequest;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Map;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import static org.assertj.core.api.Assertions.assertThat;

class MapperConfigurationTest {
    @Test
    void allMappersParseAndBindProcedureAndSearchParameters() throws Exception {
        Configuration config = new Configuration();
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/**/*.xml");
        assertThat(resources).hasSize(6);
        for (Resource resource : resources) {
            try (InputStream stream = resource.getInputStream()) {
                new XMLMapperBuilder(stream, config, resource.toString(), config.getSqlFragments()).parse();
            }
        }
        MappedStatement complete = config.getMappedStatement("com.hansol.mes.production.model.dao.ProductionResultDao.complete");
        assertThat(complete.getStatementType()).isEqualTo(StatementType.CALLABLE);
        BoundSql bound = complete.getBoundSql(Map.of("orderId", 1L,
                "request", new ProductionResultCreateRequest(97L, 3L, "외관 불량")));
        assertThat(bound.getParameterMappings()).hasSize(4);
        assertThat(bound.getSql()).contains("complete_work");
        SearchRequest search = new SearchRequest();
        search.setKeyword("' OR 1=1 --");
        search.setFrom(LocalDate.of(2026, 9, 1));
        search.setTo(LocalDate.of(2026, 9, 30));
        search.setPage(2);
        BoundSql query = config.getMappedStatement("com.hansol.mes.production.model.dao.ProductionResultDao.search").getBoundSql(search);
        assertThat(query.getSql()).doesNotContain(search.getKeyword()).contains("OFFSET ?", "FETCH NEXT ?");
        assertThat(search.getOffset()).isEqualTo(20);
    }
}

