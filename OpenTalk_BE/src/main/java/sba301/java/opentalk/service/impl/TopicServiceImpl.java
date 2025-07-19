package sba301.java.opentalk.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sba301.java.opentalk.dto.TopicDTO;
import sba301.java.opentalk.entity.Topic;
import sba301.java.opentalk.entity.User;
import sba301.java.opentalk.mapper.TopicMapper;
import sba301.java.opentalk.repository.TopicRepository;
import sba301.java.opentalk.repository.UserRepository;
import sba301.java.opentalk.service.TopicService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    @Override
    public Optional<TopicDTO> getTopic(long id) {
        return topicRepository.findById(id).map(TopicMapper.INSTANCE::toDto);
    }

    @Override
    public List<TopicDTO> getAllTopics() {
        return topicRepository.findAll()
                .stream()
                .map(t -> TopicMapper.INSTANCE.toDto(t))
                .toList();
    }

    @Override
    public TopicDTO addTopic(TopicDTO topic) {
         topicRepository.save(TopicMapper.INSTANCE.toEntity(topic));
         return topic;
    }

    @Override
    public TopicDTO updateTopic(TopicDTO topic) {
        if (topicRepository.existsById(topic.getId())) {
            topicRepository.save(TopicMapper.INSTANCE.toEntity(topic));
        }else{
            System.out.println("Not found");
        }
        return topic;
    }

    @Override
    public TopicDTO deleteTopic(long id) {
        topicRepository.deleteById(id);
        return TopicMapper.INSTANCE.toDto(topicRepository.findById(id).get());
    }


    @Override
    public List<TopicDTO> getTopicsByUser(long userId) {
        User user = userRepository.findById(userId).get();
        List<TopicDTO> listTopic = new ArrayList<>();
        List<Topic> topicEntity = topicRepository.findBySuggestedBy(user);
        for(Topic topic : topicEntity) {
            listTopic.add(TopicMapper.INSTANCE.toDto(topic));
        }
        return listTopic;
    }

    @Override
    public Page<TopicDTO> findByStatusAndTitle(String status, Pageable pageable, String title) {
        Page<Topic> topicPage = topicRepository.findByStatusAndTitle(status, pageable, title);
        return topicPage.map(TopicMapper.INSTANCE::toDto);
    }


}
