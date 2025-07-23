package sba301.java.opentalk.enums;

import sba301.java.opentalk.common.CheckPollStatus;
import sba301.java.opentalk.common.CreateMeeting;
import sba301.java.opentalk.common.RandomHostSelectionJob;
import sba301.java.opentalk.common.SyncDataUserFromHRM;

public enum CronKey {
    RANDOM(
            "randomHostSelectionJob",
            "randomHostSelectionTrigger",
            RandomHostSelectionJob.class,
            "0 0 10 ? * TUE"
    ),
    CHECK_POLL(
            "checkPollStatus",
            "updatePollStatusTrigger",
            CheckPollStatus.class,
            "0 0 0 ? * TUE"
    ),
    CREATE_MEETING(
            "createMeeting",
            "createMeetingTrigger",
            CreateMeeting.class,
            "0 0 0 ? * MON"
    ),
    SYNC(
            "syncJob",
            "syncTrigger",
            SyncDataUserFromHRM.class,
            "59 34 14 * * ?"
    );

    private final String jobName;
    private final String triggerName;
    private final Class<? extends org.quartz.Job> jobClass;
    private final String defaultExpression;

    CronKey(String jobName, String triggerName, Class<? extends org.quartz.Job> jobClass, String defaultExpression) {
        this.jobName = jobName;
        this.triggerName = triggerName;
        this.jobClass = jobClass;
        this.defaultExpression = defaultExpression;
    }

    public String getJobName() {
        return jobName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public Class<? extends org.quartz.Job> getJobClass() {
        return jobClass;
    }

    public String getDefaultExpression() {
        return defaultExpression;
    }

    public String getKey() {
        return name();
    }
}
