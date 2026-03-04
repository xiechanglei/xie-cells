package io.github.xiechanglei.cell.starter.jpa.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * jpa查询帮助类，主要是为了使动态查询更加方便，
 *
 * @author xie
 * @date 2025/3/24
 */

@Component
@RequiredArgsConstructor
public class CellJpaQuery {
    /**
     * jpa的entityManager，需要提供
     **/
    private final EntityManager entityManager;

    public JpaQueryProducer createNamedQuery(String qlString) {
        return new JpaQueryProducerImpl(entityManager, false, qlString.trim());
    }

    public JpaQueryProducer createNativeQuery(String sql) {
        return new JpaQueryProducerImpl(entityManager, true, sql.trim());
    }

    public void use(){
        createNamedQuery("select * from xxx")
                .withBoolCheckCondition(false, "and l.userId = :userId")
                .param("userId", 1)
                .getTupleList(null);
    }
}
