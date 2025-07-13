package sba301.java.opentalk.model.Mail;

public class MailRemind implements MailSubject {
    @Override
    public String generateSubjectOfMail() {
        return "This is mail remind you to join next OpenTalk";
    }
}
