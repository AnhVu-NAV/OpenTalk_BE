package sba301.java.opentalk.common;

import lombok.RequiredArgsConstructor;
import sba301.java.opentalk.service.OpenTalkMeetingService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RandomHostSelectionJob implements Job {
    private final OpenTalkMeetingService meetingService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        meetingService.createScheduledOpenTalk();
    }
}
