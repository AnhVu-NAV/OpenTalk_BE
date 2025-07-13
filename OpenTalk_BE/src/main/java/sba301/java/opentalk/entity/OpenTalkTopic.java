package sba301.java.opentalk.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "opentalk_topic")
public class OpenTalkTopic extends BaseEntity {
    @Column(name = "topic_name", unique = true, nullable = false, length = 255)
    private String topicName;

    @ManyToOne
    @JoinColumn(name = "company_branch_id", nullable = false)
    private CompanyBranch companyBranch;

    @Column(name = "scheduled_date", nullable = false)
    private LocalDate scheduledDate;

    @Column(name = "meeting_link", length = 255)
    private String meetingLink;

    @Column(name = "is_enabled", nullable = false)
    private boolean isEnabled = true;

    public OpenTalkTopic(long id) {
        this.setId(id);
    }
}
