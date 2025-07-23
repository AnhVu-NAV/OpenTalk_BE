package sba301.java.opentalk.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.context.annotation.Configuration;
import sba301.java.opentalk.enums.CronKey;
import sba301.java.opentalk.service.CronExpressionService;

@Configuration
@RequiredArgsConstructor
public class QuartzConfig {

    private final CronExpressionService cronService;
    private final Scheduler scheduler;

    @PostConstruct
    public void init() throws SchedulerException {
        for (CronKey key : CronKey.values()) {
            JobDetail jd = JobBuilder.newJob(key.getJobClass())
                    .withIdentity(key.getJobName())
                    .storeDurably()
                    .build();
            if (!scheduler.checkExists(jd.getKey())) {
                scheduler.addJob(jd, false);
            }
            String expr = cronService.getCronExpression(key);
            TriggerKey tk = TriggerKey.triggerKey(key.getTriggerName());
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(tk)
                    .forJob(jd)
                    .withSchedule(CronScheduleBuilder.cronSchedule(expr))
                    .build();
            if (scheduler.checkExists(tk)) {
                scheduler.rescheduleJob(tk, trigger);
            } else {
                scheduler.scheduleJob(trigger);
            }
        }
    }
}
