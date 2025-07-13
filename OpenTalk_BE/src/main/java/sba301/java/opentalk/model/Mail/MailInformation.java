package sba301.java.opentalk.model.Mail;

public class MailInformation implements MailSubject {
    @Override
    public String generateSubjectOfMail() {
        return "This is mail information about you to join next OpenTalk";
    }
}
