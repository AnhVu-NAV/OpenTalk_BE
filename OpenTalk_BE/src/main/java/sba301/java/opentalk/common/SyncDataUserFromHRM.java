package sba301.java.opentalk.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sba301.java.opentalk.exception.AppException;
import sba301.java.opentalk.serverHrm.service.HrmService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SyncDataUserFromHRM implements Job {
    private final HrmService hrmService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            hrmService.syncUsersFromHrm();
        } catch (AppException e) {
            log.error("Sync job failed: {}", e.getMessage());
            throw new JobExecutionException("Sync job failed", e);
        }
    }
}
