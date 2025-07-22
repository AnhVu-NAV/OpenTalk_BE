package sba301.java.opentalk.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import sba301.java.opentalk.enums.MeetingStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "opentalk_meeting")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OpenTalkMeeting extends BaseEntity {
    @Column(name = "meeting_name", unique = true, nullable = false, length = 255)
    private String meetingName;

    @Column(name = "scheduled_date", nullable = false)
    private LocalDateTime scheduledDate;

    @Column(name = "meeting_link", length = 255)
    private String meetingLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private MeetingStatus status;

    @OneToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User host;

    @Column(name = "duration", nullable = false)
    private double duration;

    @ManyToOne
    @JoinColumn(name = "company_branch_id", nullable = false)
    private CompanyBranch companyBranch;

    public OpenTalkMeeting(long id) {
        this.setId(id);
    }
}