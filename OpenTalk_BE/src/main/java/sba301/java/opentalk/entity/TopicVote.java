package sba301.java.opentalk.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "topic_vote",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "topic_poll_id"}))
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TopicVote extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User voter;

    @ManyToOne
    @JoinColumn(name = "topic_poll_id")
    private TopicPoll topicPoll;
}