package sba301.java.opentalk.service;

import sba301.java.opentalk.dto.OpenTalkMeetingDTO;
import sba301.java.opentalk.model.Mail.Mail;

public interface MailService {
    void sendMail(Mail mail);

    void sendMailUpdateInfoMeetingForMeetingManager(OpenTalkMeetingDTO openTalkMeetingDTO);
}
