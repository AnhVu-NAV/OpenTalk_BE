package sba301.java.opentalk.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "topic_poll")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class TopicPoll extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "poll_id")
    private Poll Poll;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

}