package sba301.java.opentalk.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sba301.java.opentalk.dto.PollDTO;
import sba301.java.opentalk.entity.Poll;
import sba301.java.opentalk.mapper.PollMapper;
import sba301.java.opentalk.repository.PollRepository;
import sba301.java.opentalk.service.PollService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PollServiceImpl implements PollService {
    private final PollRepository pollRepository;
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
}
