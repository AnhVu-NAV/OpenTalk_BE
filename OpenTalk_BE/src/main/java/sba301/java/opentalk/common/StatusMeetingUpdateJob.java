package sba301.java.opentalk.common;

import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import sba301.java.opentalk.service.OpenTalkMeetingService;

@Component
@RequiredArgsConstructor
public class StatusMeetingUpdateJob implements Job {
    private final OpenTalkMeetingService openTalkMeetingService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Long meetingId = Long.valueOf(jobExecutionContext.getJobDetail().getKey().getName());
        String jobType = (String) jobExecutionContext.getJobDetail().getJobDataMap().get("jobType");

        openTalkMeetingService.updateMeetingStatus(meetingId, jobType);
    }
}
