package io.github.xiechanglei.cell.common.jpa.bean.generator;

/**
 * 雪花 ID 生成器
 * <p>
 * 该类实现了雪花算法用于生成唯一的分布式 ID。雪花算法的核心思想是将时间戳、数据中心 ID、机器 ID 和序列号组合在一起，生成一个唯一的 64 位 ID。
 * </p>
 */
public class SnowFlake {

    // 起始时间戳，用于计算生成 ID 的时间戳部分
    private final static long START_TIMESTAMP = 1480166465631L;

    // 每部分占用的位数（符号位不算在内）
    private final static long SEQUENCE_BIT = 12; // 序列号部分的位数
    private final static long MACHINE_BIT = 5; // 机器标识部分的位数
    private final static long DATACENTER_BIT = 5; // 数据中心标识部分的位数

    // 每部分的最大值
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT); // 序列号最大值
    private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT); // 机器标识最大值
    private final static long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT); // 数据中心标识最大值

    // 各部分左移的位数
    private final static long MACHINE_LEFT = SEQUENCE_BIT; // 机器标识左移位数
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT; // 数据中心标识左移位数
    private final static long TIMESTAMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT; // 时间戳左移位数

    private final long datacenterId; // 数据中心 ID
    private final long machineId; // 机器标识 ID
    private long sequence = 0L; // 序列号
    private long lastTimestamp = -1L; // 上一个时间戳

    /**
     * 构造函数
     *
     * @param datacenterId 数据中心 ID
     * @param machineId    机器标识 ID
     */
    public SnowFlake(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("数据中心 ID 超过最大值或小于 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("机器标识 ID 超过最大值或小于 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    /**
     * 生成下一个唯一 ID
     *
     * @return 生成的唯一 ID
     */
    public synchronized long nextId() {
        long currTimestamp = getTimestamp();
        if (currTimestamp < lastTimestamp) {
            throw new RuntimeException("时钟回拨。拒绝生成 ID");
        }

        // 如果是同一时间生成的，则进行毫秒内序列号的处理
        if (currTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 序列号达到最大值，等待下一个毫秒
            if (sequence == 0L) {
                currTimestamp = getNextTimestamp();
            }
        } else {
            // 时间戳改变，重置序列号
            sequence = 0L;
        }

        // 更新上一次生成 ID 的时间戳
        lastTimestamp = currTimestamp;

        // 生成唯一 ID
        return (currTimestamp - START_TIMESTAMP) << TIMESTAMP_LEFT // 时间戳部分
                | datacenterId << DATACENTER_LEFT // 数据中心部分
                | machineId << MACHINE_LEFT // 机器标识部分
                | sequence; // 序列号部分
    }


    /**
     * 获取下一个时间戳（确保时间戳在上一次时间戳之后）
     *
     * @return 下一个时间戳
     */
    private long getNextTimestamp() {
        long timestamp = getTimestamp();
        while (timestamp <= lastTimestamp) {
            timestamp = getTimestamp();
        }
        return timestamp;
    }

    /**
     * 获取当前的时间戳
     *
     * @return 当前的时间戳（毫秒）
     */
    private long getTimestamp() {
        return System.currentTimeMillis();
    }
}
