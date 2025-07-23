package sba301.java.opentalk.common;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.context.annotation.Configuration;
import sba301.java.opentalk.enums.CronKey;
import sba301.java.opentalk.service.CronExpressionService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DynamicCronUpdater {

    private final Scheduler scheduler;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
    private final CronExpressionService cronExpressionService;

    @PostConstruct
    public void startCronUpdateTasks() {
        log.info("Starting cron update tasks");
        executorService.scheduleAtFixedRate(this::updateDynamicCronJobs, 0, 10, TimeUnit.MINUTES);
    }

    private void updateDynamicCronJobs() {
        try {
            for (CronKey cronKey : CronKey.values()) {
                String threadName = Thread.currentThread().getName();
                long threadId = Thread.currentThread().getId();

                log.info("Thread [{} - {}] is processing update dynamic cron for {}", threadName, threadId, cronKey.getKey());

                String newCronExpression = cronExpressionService.getCronExpression(cronKey);

                TriggerKey triggerKey = TriggerKey.triggerKey(cronKey.getTriggerName());
                CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

                log.info("New Cron Expression for {}: {}", cronKey.getKey(), newCronExpression);

                if (trigger != null && newCronExpression != null && !trigger.getCronExpression().equals(newCronExpression)) {
                    Trigger newTrigger = TriggerBuilder.newTrigger()
                            .forJob(trigger.getJobKey())
                            .withIdentity(triggerKey)
                            .withSchedule(CronScheduleBuilder.cronSchedule(newCronExpression))
                            .build();

                    scheduler.rescheduleJob(triggerKey, newTrigger);
                }
            }
        } catch (Exception e) {
            log.error("Failed to update dynamic cron jobs", e);
        }
    }

    @PreDestroy
    public void stopCronUpdateTasks() {
        executorService.shutdown();
        log.info("Cron update tasks stopped.");
    }
}