package sba301.java.opentalk.common;

import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import sba301.java.opentalk.dto.PollDTO;
import sba301.java.opentalk.service.PollService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CheckPollStatus implements Job {
    private final PollService pollService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<PollDTO> listPoll = pollService.getAll();
        for (PollDTO poll : listPoll) {
            pollService.updatePollStatus(poll);
        }
    }
}
