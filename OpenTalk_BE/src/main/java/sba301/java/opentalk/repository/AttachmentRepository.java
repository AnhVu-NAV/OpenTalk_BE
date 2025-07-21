package sba301.java.opentalk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sba301.java.opentalk.entity.Attachment;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByOpenTalkMeetingId(Long meetingId);
}
