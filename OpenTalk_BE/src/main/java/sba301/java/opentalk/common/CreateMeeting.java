package sba301.java.opentalk.common;

import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import sba301.java.opentalk.service.OpenTalkMeetingService;

@Component
@RequiredArgsConstructor
public class CreateMeeting implements Job {
    private final OpenTalkMeetingService meetingService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        meetingService.createEmptyOpenTalk();
    }
}
