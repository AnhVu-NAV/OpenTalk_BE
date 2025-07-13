package sba301.java.opentalk.model.Mail;

import sba301.java.opentalk.enums.MailType;

public class MailSubjectFactory {
    public static final MailSubject getMailSubject(MailType mailType) {
        return switch (mailType) {
            case REMIND -> new MailRemind();
            case INVITATION -> new MailInvitation();
            case INFORMATION -> new MailInformation();
        };
    }
}
