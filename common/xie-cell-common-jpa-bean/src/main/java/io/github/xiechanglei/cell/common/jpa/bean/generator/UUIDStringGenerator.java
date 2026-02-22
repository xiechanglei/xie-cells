package io.github.xiechanglei.cell.common.jpa.bean.generator;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochRandomGenerator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * 自定义 UUID 生成器,使用UUID v7 实现方案
 * </p>
 */
public class UUIDStringGenerator implements IdentifierGenerator {
    //timeBasedEpochGenerator 这里在一个时间节点下产生的 UUID 是，并不一定是严格递增的 todo
    TimeBasedEpochRandomGenerator timeBasedEpochRandomGenerator = Generators.timeBasedEpochRandomGenerator();

    /**
     * 这里的 generate 方法会在每次插入数据时被调用，替换掉了-，性能略微有一些下降,主要是保持全局jpa的entity中使用string类型的变量，这样减少代码的复杂程度
     */
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        return timeBasedEpochRandomGenerator.generate().toString().replace("-", "");
    }

}
