package com.star.lp.module.trade.framework.store.job;

import cn.hutool.core.collection.CollUtil;
import com.star.lp.framework.quartz.core.handler.JobHandler;
import com.star.lp.framework.tenant.core.job.TenantJob;
import com.star.lp.module.trade.dal.dataobject.print.PrintQueueDO;
import com.star.lp.module.trade.dal.mysql.print.PrintQueueMapper;
import com.star.lp.framework.mybatis.core.query.LambdaQueryWrapperX;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 打印队列清理任务
 */
@Component
@Slf4j
public class PrintQueueCleanJob implements JobHandler {

    @Resource
    private PrintQueueMapper printQueueMapper;

    @Override
    public String execute(String param) throws Exception {
        log.info("[PrintQueueCleanJob] 开始执行打印队列清理");

        // 清理7天前已完成的任务
        LocalDateTime cleanTime = LocalDateTime.now().minusDays(7);

        List<PrintQueueDO> expiredTasks = printQueueMapper.selectList(
                new LambdaQueryWrapperX<PrintQueueDO>()
                        .in(PrintQueueDO::getStatus, "completed", "cancelled", "failed")
                        .lt(PrintQueueDO::getCompleteTime, cleanTime)
                        .eq(PrintQueueDO::getDeleted, false)
        );

        if (CollUtil.isEmpty(expiredTasks)) {
            log.info("[PrintQueueCleanJob] 没有需要清理的队列任务");
            return "没有需要清理的队列任务";
        }

        // 标记为已删除
        for (PrintQueueDO task : expiredTasks) {
            printQueueMapper.updateById(
                    new PrintQueueDO().setId(task.getId()).setDeleted(true)
            );
        }

        String result = String.format("打印队列清理完成，清理了 %d 个过期任务", expiredTasks.size());
        log.info("[PrintQueueCleanJob] {}", result);
        return result;
    }
}