package io.github.xiechanglei.cell.starter.jpa.log;

import io.github.xiechanglei.cell.starter.spring.log.FileMarker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import org.slf4j.Marker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 类的详细说明
 *
 * @author xie
 * @date 2026/1/27
 */
@Component
@Slf4j
@ConditionalOnProperty(prefix = "jpa.log", name = "enable", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class CellQueryExecutionListener implements QueryExecutionListener {
    private final CellSqlLogProperties cellSqlLogProperties;

    public static final Marker marker = FileMarker.of("jpa.sql");

    @Override
    public void beforeQuery(ExecutionInfo executionInfo, List<QueryInfo> list) {
    }

    @Override
    public void afterQuery(ExecutionInfo executionInfo, List<QueryInfo> list) {
        if (!list.isEmpty()) {
            if (list.size() == 1) {
                QueryInfo queryInfo = list.getFirst();
                doLog("ExecutionTime: " + executionInfo.getElapsedTime() + "ms\tSQL: " + queryInfo.getQuery(), executionInfo.getElapsedTime());
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("ExecutionTime: ").append(executionInfo.getElapsedTime()).append("ms\n");
                for (QueryInfo queryInfo : list) {
                    sb.append("SQL: ").append(queryInfo.getQuery()).append("\n");
                }
                doLog(sb.toString(), executionInfo.getElapsedTime());
            }
        }
    }

    private void doLog(String logMessage, long executionTime) {
        if (executionTime < cellSqlLogProperties.getLowSqlLimit()) {
            log.info(marker, logMessage);
        } else {
            log.warn(marker, "Slow SQL Detected! {}", logMessage);
        }
    }
}
