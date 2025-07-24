package sba301.java.opentalk.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import sba301.java.opentalk.dto.OpenTalkMeetingDTO;
import sba301.java.opentalk.dto.UserDTO;
import sba301.java.opentalk.enums.MailType;
import sba301.java.opentalk.model.Mail.Mail;
import sba301.java.opentalk.model.Mail.MailSubjectFactory;
import sba301.java.opentalk.service.MailService;
import sba301.java.opentalk.service.UserService;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final UserService userService;

    @Value("${spring.mail.username}")
    private String defaultMailFrom;

    @Override
    public void sendMail(Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(defaultMailFrom);
        message.setTo(mail.getMailTo());
        message.setSubject(mail.getMailSubject());
        message.setText(mail.getMailContent());

        mailSender.send(message);
    }

    @Override
    public void sendMailUpdateInfoMeetingForMeetingManager(OpenTalkMeetingDTO openTalkMeetingDTO) {
        Mail mail = new Mail();
        mail.setMailTo(userService.getAllMeetingManagers().stream().map(UserDTO::getEmail).toArray(String[]::new));
        mail.setMailSubject(MailSubjectFactory.getMailSubject(MailType.REMIND).toString());
        mail.setMailContent("Please update information and choose topic vote for Meeting: " + openTalkMeetingDTO.getMeetingName());
        this.sendMail(mail);
    }

    @Override
    public void sendPasswordResetMail(String email, String token) {
        String resetLink = "http://localhost:5173/reset-password?token=" + token;

        Mail mail = new Mail();
        mail.setMailTo(new String[]{email});
        mail.setMailSubject("Password Reset Request");
        mail.setMailContent("Click the following link to reset your password: " + resetLink);

        this.sendMail(mail);
    }
}
