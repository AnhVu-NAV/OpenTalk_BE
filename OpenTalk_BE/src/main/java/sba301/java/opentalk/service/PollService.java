package sba301.java.opentalk.service;

import sba301.java.opentalk.dto.PollDTO;

import java.util.List;

public interface PollService {
    public PollDTO getPollByMeeting(long meetingId);
    public PollDTO findById(long id);
    public List<PollDTO> getAll();
}
