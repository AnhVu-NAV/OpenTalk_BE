package sba301.java.opentalk.config;

import lombok.RequiredArgsConstructor;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sba301.java.opentalk.common.CheckPollStatus;
import sba301.java.opentalk.common.RandomHostSelectionJob;
import sba301.java.opentalk.common.SyncDataUserFromHRM;
import sba301.java.opentalk.service.RedisService;

@Configuration
@RequiredArgsConstructor
public class QuartzConfig {
    private final RedisService redisService;

    @Bean
    public JobDetail randomHostJobDetail() {
        return JobBuilder.newJob(RandomHostSelectionJob.class)
                .withIdentity("randomHostSelectionJob")
                .storeDurably()
                .build();
    }

    @Bean
    public JobDetail updatePollResultJobDetail() {
        return JobBuilder.newJob(CheckPollStatus.class)
                .withIdentity("checkPollStatus")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger updatePollStatusTrigger(@Qualifier("updatePollResultJobDetail") JobDetail jobDetail) {
        String cronExpression = redisService.getRandomDateCron();
        if (cronExpression == null) {
            cronExpression = "0 0 0 ? * TUE";
        }

        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("updatePollStatusTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();
    }

    @Bean
    public Trigger randomTrigger(@Qualifier("randomHostJobDetail") JobDetail jobDetail) {
        String cronExpression = redisService.getRandomDateCron();
        if (cronExpression == null) {
            cronExpression = "0 0 10 ? * TUE";
        }

        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("randomHostSelectionTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();
    }

    @Bean
    public JobDetail syncDataJobDetail() {
        return JobBuilder.newJob(SyncDataUserFromHRM.class)
                .withIdentity("syncJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger syncTrigger(@Qualifier("syncDataJobDetail") JobDetail jobDetail) {
        String cronExpression = redisService.getSyncDateCron();
        if (cronExpression == null) {
            cronExpression = "59 34 14 * * ?";
        }

        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("syncTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();
    }
}
