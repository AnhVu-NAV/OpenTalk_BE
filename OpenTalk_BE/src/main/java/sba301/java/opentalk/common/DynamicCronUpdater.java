package sba301.java.opentalk.common;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sba301.java.opentalk.service.RedisService;
import org.quartz.*;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DynamicCronUpdater {

    private final Scheduler scheduler;
    private final RedisService redisService;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

    @PostConstruct
    public void startCronUpdateTasks() {
        log.info("Starting cron update tasks");
        executorService.scheduleAtFixedRate(this::updateDynamicRandomSelectionHostCron, 0, 10, TimeUnit.MINUTES);
        executorService.scheduleAtFixedRate(this::updateDynamicSyncDataCron, 0, 10, TimeUnit.MINUTES);
    }

    private void updateDynamicRandomSelectionHostCron() {
        try {
            String threadName = Thread.currentThread().getName();
            long threadId = Thread.currentThread().getId();

            log.info("Thread [{} - {}] is processing update dynamic random selection host cron", threadName, threadId);

            String newRandomDateCron = redisService.getRandomDateCron();
            TriggerKey triggerKey = TriggerKey.triggerKey("randomHostSelectionTrigger");

            log.info("New Random Date Cron: {}", newRandomDateCron);

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger != null && newRandomDateCron != null && !trigger.getCronExpression().equals(newRandomDateCron)) {
                Trigger newTrigger = TriggerBuilder.newTrigger()
                        .forJob(trigger.getJobKey())
                        .withIdentity(triggerKey)
                        .withSchedule(CronScheduleBuilder.cronSchedule(newRandomDateCron))
                        .build();

                scheduler.rescheduleJob(triggerKey, newTrigger);
            }
        } catch (Exception e) {
            log.error("Failed to update random selection host cron", e);
        }
    }

    private void updateDynamicSyncDataCron() {
        try {
            String threadName = Thread.currentThread().getName();
            long threadId = Thread.currentThread().getId();

            log.info("Thread [{} - {}] is processing update dynamic sync data cron", threadName, threadId);

            String newSyncDateCron = redisService.getSyncDateCron();
            TriggerKey triggerKey = TriggerKey.triggerKey("syncTrigger");

            log.info("New Sync Date Cron: {}", newSyncDateCron);

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger != null && newSyncDateCron != null && !trigger.getCronExpression().equals(newSyncDateCron)) {
                Trigger newTrigger = TriggerBuilder.newTrigger()
                        .forJob(trigger.getJobKey())
                        .withIdentity(triggerKey)
                        .withSchedule(CronScheduleBuilder.cronSchedule(newSyncDateCron))
                        .build();

                scheduler.rescheduleJob(triggerKey, newTrigger);
            }
        } catch (Exception e) {
            log.error("Failed to update sync data cron", e);
        }
    }

    @PreDestroy
    public void stopCronUpdateTasks() {
        executorService.shutdown();
        log.info("Cron update tasks stopped.");
    }
}
