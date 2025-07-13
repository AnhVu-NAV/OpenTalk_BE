package sba301.java.opentalk.common;

import lombok.RequiredArgsConstructor;
import sba301.java.opentalk.service.OpenTalkTopicService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RandomHostSelectionJob implements Job {
    private final OpenTalkTopicService topicService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        topicService.createScheduledOpenTalk();
    }
}
