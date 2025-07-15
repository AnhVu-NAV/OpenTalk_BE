package sba301.java.opentalk.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "opentalk_slide")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class OpenTalkSlide extends BaseEntity {
    @Column(nullable = false, length = 255)
    private String fileName;

    @Column(nullable = false, length = 500)
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    private OpenTalkMeeting meeting;

    public OpenTalkSlide(long id) {
        this.setId(id);
    }
}