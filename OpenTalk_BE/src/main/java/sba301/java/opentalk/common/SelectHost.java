package sba301.java.opentalk.common;

import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import sba301.java.opentalk.dto.UserDTO;
import sba301.java.opentalk.enums.MailType;
import sba301.java.opentalk.enums.MeetingStatus;
import sba301.java.opentalk.model.Mail.Mail;
import sba301.java.opentalk.model.Mail.MailSubjectFactory;
import sba301.java.opentalk.service.MailService;
import sba301.java.opentalk.service.OpenTalkMeetingService;
import sba301.java.opentalk.service.UserService;

@Component
@RequiredArgsConstructor
public class SelectHost implements Job {
    private final MailService mailService;
    private final OpenTalkMeetingService openTalkMeetingService;
    private final UserService userService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        openTalkMeetingService.getAllMeetingsByStatus(MeetingStatus.WAITING_HOST_REGISTER).forEach(meeting -> {
            Mail mail = new Mail();
            mail.setMailTo(userService.getAllMeetingManagers().stream().map(UserDTO::getEmail).toArray(String[]::new));
            mail.setMailSubject(MailSubjectFactory.getMailSubject(MailType.REMIND).toString());
            mail.setMailContent("Please process host registration application: " + meeting.getMeetingName());
            mailService.sendMail(mail);
            meeting.setStatus(MeetingStatus.WAITING_HOST_SELECTION);
            openTalkMeetingService.updateMeeting(meeting, meeting.getId());
        });
    }
}
