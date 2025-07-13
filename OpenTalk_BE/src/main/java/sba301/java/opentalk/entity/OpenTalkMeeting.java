package sba301.java.opentalk.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    private LocalDate scheduledDate;

    @Column(name = "meeting_link", length = 255)
    private String meetingLink;

    @Column(name = "status", nullable = false)
    private String status;

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