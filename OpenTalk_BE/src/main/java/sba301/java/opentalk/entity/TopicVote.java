package sba301.java.opentalk.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "topic_vote")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class TopicVote extends BaseEntity{
    @OneToOne
    @JoinColumn(name = "user_id")
    private User voter;

    @OneToOne
    @JoinColumn(name = "topic_poll_id")
    private TopicPoll topicPoll;
}