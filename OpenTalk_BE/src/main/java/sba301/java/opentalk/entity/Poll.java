package sba301.java.opentalk.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "poll")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Poll extends BaseEntity{
    @OneToOne
    @JoinColumn(name = "opentalk_meeting_id")
    private OpenTalkMeeting openTalkMeeting;

    @Column
    private boolean isEnabled;

}
