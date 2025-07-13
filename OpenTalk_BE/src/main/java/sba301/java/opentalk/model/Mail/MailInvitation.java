package sba301.java.opentalk.model.Mail;

public class MailInvitation implements MailSubject {
    @Override
    public String generateSubjectOfMail() {
        return "This is mail invitation about you to join next OpenTalk";
    }
}
