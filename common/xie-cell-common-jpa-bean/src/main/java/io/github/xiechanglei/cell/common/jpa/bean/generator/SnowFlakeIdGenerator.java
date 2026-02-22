package io.github.xiechanglei.cell.common.jpa.bean.generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * JPA 雪花算法 ID 生成器
 * <p>
 * 该类实现了 Hibernate 的 IdentifierGenerator 接口，使用雪花算法生成唯一的 ID。
 * 在 JPA 实体中，通常用作主键生成策略。
 * </p>
 */
public class SnowFlakeIdGenerator implements IdentifierGenerator {

    // 静态的 SnowFlake 实例，用于生成唯一 ID
    public static final SnowFlake snowFlake = new SnowFlake(1, 1);

    /**
     * 生成唯一 ID
     * <p>
     * 该方法使用雪花算法生成唯一的 ID，并返回该 ID。它会被 Hibernate 在实体持久化时调用。
     * </p>
     *
     * @param session Hibernate 的会话接口，用于生成 ID
     * @param o       需要生成 ID 的实体对象（通常在这里不会使用该对象）
     * @return 生成的唯一 ID
     * @throws HibernateException 如果生成 ID 时发生异常
     */
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object o) throws HibernateException {
        // 调用雪花算法生成下一个 ID
        return snowFlake.nextId();
    }
}
