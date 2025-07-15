package sba301.java.opentalk.event.handle;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import sba301.java.opentalk.dto.OpenTalkMeetingDTO;
import sba301.java.opentalk.dto.UserDTO;
import sba301.java.opentalk.enums.HostRegistrationStatus;
import sba301.java.opentalk.event.HostRegistrationEvent;
import sba301.java.opentalk.model.Mail.Mail;
import sba301.java.opentalk.service.MailService;
import sba301.java.opentalk.service.UserService;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class HostRegistrationEventListener {
    private final MailService mailService;
    private final UserService userService;
    private final MessageSource messageSource;

    @Async
    @EventListener
    public void handleHostRegistrationEvent(HostRegistrationEvent event) {
        UserDTO user = event.getUser();
        OpenTalkMeetingDTO meeting = event.getTopic();
        Locale locale = LocaleContextHolder.getLocale();
        Mail mail = new Mail();

        if (event.getStatus().equals(HostRegistrationStatus.APPROVED)) {
            mail.setMailSubject(messageSource.getMessage("mail.subject.registration.approved", null, locale));
            mail.setMailContent(messageSource.getMessage("mail.content.registration.approved",
                    new Object[]{user.getFullName(), meeting.getMeetingName(), meeting.getScheduledDate()}, locale));
            mail.setMailTo(new String[]{user.getEmail()});
        } else if (event.getStatus().equals(HostRegistrationStatus.REJECTED)) {
            mail.setMailSubject(messageSource.getMessage("mail.subject.registration.rejected", null, locale));
            mail.setMailContent(messageSource.getMessage("mail.content.registration.rejected",
                    new Object[]{user.getFullName(), meeting.getMeetingName(), meeting.getScheduledDate()}, locale));
            mail.setMailTo(new String[]{user.getEmail()});
        } else {
            mail.setMailSubject("Host Meeting Registration Announcement");
            List<UserDTO> admins = userService.getAllAdmin();
            mail.setMailTo(admins.stream()
                    .map(UserDTO::getEmail)
                    .toArray(String[]::new));
            mail.setMailContent("Please check " + meeting.getMeetingName() + " - ID: " + meeting.getId() +
                    "to confirm request to be Host of " + user.getFullName() + " -ID: " + user.getId());
        }
        mailService.sendMail(mail);
    }
}
