package sba301.java.opentalk.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import sba301.java.opentalk.enums.OpenTalkRegistrationStatus;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "opentalk_registration")
public class OpenTalkRegistration extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "opentalk_topic_id", nullable = false)
    private OpenTalkTopic openTalkTopic;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private OpenTalkRegistrationStatus status;

    public OpenTalkRegistration(long id) {
        this.setId(id);
    }
}
