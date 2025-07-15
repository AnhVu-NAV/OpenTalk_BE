package sba301.java.opentalk.entity;

import jakarta.persistence.*;
import lombok.*;
import sba301.java.opentalk.enums.MeetingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "opentalk_meeting")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @ManyToOne
    @JoinColumn(name = "company_branch_id", nullable = false)
    private CompanyBranch companyBranch;

    public OpenTalkMeeting(long id) {
        this.setId(id);
    }
}