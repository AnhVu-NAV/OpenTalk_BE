package sba301.java.opentalk.entity;

import jakarta.persistence.*;
import lombok.*;
import sba301.java.opentalk.enums.HostRegistrationStatus;

@Entity
@Table(name = "host_registration")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HostRegistration extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "opentalk_meeting_id", nullable = false)
    private OpenTalkMeeting openTalkMeeting;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private HostRegistrationStatus status;
}
