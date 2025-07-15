package sba301.java.opentalk.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "topic_vote")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class TopicVote extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User voter;

    @ManyToOne
    @JoinColumn(name = "topic_poll_id")
    private TopicPoll topicPoll;
}