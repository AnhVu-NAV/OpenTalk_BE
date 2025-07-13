package sba301.java.opentalk.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "topic_poll")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class TopicPoll extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "opentalk_meeting_id")
    private OpenTalkMeeting openTalkMeeting;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;
}