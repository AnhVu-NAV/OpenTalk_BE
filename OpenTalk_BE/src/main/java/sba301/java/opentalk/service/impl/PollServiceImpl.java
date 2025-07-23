package sba301.java.opentalk.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sba301.java.opentalk.dto.OpenTalkMeetingDTO;
import sba301.java.opentalk.dto.PollDTO;
import sba301.java.opentalk.entity.*;
import sba301.java.opentalk.enums.MeetingStatus;
import sba301.java.opentalk.mapper.OpenTalkMeetingMapper;
import sba301.java.opentalk.mapper.PollMapper;
import sba301.java.opentalk.mapper.TopicMapper;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import sba301.java.opentalk.repository.*;
import sba301.java.opentalk.service.PollService;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PollServiceImpl implements PollService {
    private final PollRepository pollRepository;
    private final OpenTalkMeetingRepository openTalkMeetingRepository;
    private final TopicPollRepository topicPollRepository;
    private final TopicVoteRepository topicVoteRepository;
    private final UserRepository userRepository;

    @Override
    public PollDTO getPollByMeeting(long meetingId) {
        Poll poll = pollRepository.findByOpenTalkMeetingId(meetingId);
        return PollMapper.INSTANCE.toDto(poll);
    }

    @Override
    public PollDTO findById(long id) {
        PollDTO dto = PollMapper.INSTANCE.toDto(pollRepository.findByOpenTalkMeetingId((int)id));
        return dto;
    }

    @Override
    public List<PollDTO> getAll() {
        return pollRepository.findAll().stream().map(PollMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    @Override
    public void updatePollStatus(PollDTO pollDTO) {
        OpenTalkMeetingDTO openTalkMeetingDTO = pollDTO.getOpenTalkMeeting();
        if(pollDTO.isEnabled() && MeetingStatus.WAITING_TOPIC.equals(openTalkMeetingDTO.getStatus()) &&
           LocalDateTime.now().isAfter(openTalkMeetingDTO.getScheduledDate().minusDays(2))) {
            Poll poll = PollMapper.INSTANCE.toEntity(pollDTO);
            List<TopicPoll> listTopicPoll = topicPollRepository.findByPollId(poll.getId());
            HashMap<TopicPoll, Long> resultTopicPoll = new HashMap<>();
            for (TopicPoll topicPoll : listTopicPoll) {
                resultTopicPoll.put(topicPoll, topicVoteRepository.countByTopicPoll(topicPoll));
            }
            Map.Entry<TopicPoll, Long> maxEntry = Collections.max(
                    resultTopicPoll.entrySet(),
                    Map.Entry.comparingByValue()
            );
            TopicPoll topicPoll = maxEntry.getKey();
            openTalkMeetingDTO.setTopic(TopicMapper.INSTANCE.toDto(topicPoll.getTopic()));
            poll.setEnabled(false);
            poll.setOpenTalkMeeting(OpenTalkMeetingMapper.INSTANCE.toEntity(openTalkMeetingDTO));
            pollRepository.save(poll);
        }
    }

    @Override
    public PollDTO createPoll(long meetingId) {
        OpenTalkMeeting openTalkMeeting = openTalkMeetingRepository.findById(meetingId).get();
        Poll poll = new Poll();
        poll.setOpenTalkMeeting(openTalkMeeting);
        poll.setEnabled(true);
        return PollMapper.INSTANCE.toDto(poll);
    }

    @Override
    public boolean checkVoteAbility(long pollid, long userId) {
        List<TopicPoll> listAnswer = topicPollRepository.findByPollId(pollid);
        Poll poll = pollRepository.findById((int)pollid).get();
        for(TopicPoll topicPoll : listAnswer) {
            List<TopicVote> topicVote = topicVoteRepository.findByTopicPollIdAndVoterId(topicPoll.getId(), userId);
            if(!topicVote.isEmpty()) {
                return false;
            }
        }
        return poll.isEnabled();
    }


}
