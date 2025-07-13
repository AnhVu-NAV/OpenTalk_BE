package sba301.java.opentalk.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "topic")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Topic extends BaseEntity {
    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255)
    private String description;

    @Column
    private String status;

    @Column
    private String remark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_suggest_id")
    private User suggestBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_evalute_id")
    private User evaluteBy;
}