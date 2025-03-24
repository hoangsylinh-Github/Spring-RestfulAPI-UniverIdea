package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Topic;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.topic.ResCreateTopicDTO;
import vn.hoidanit.jobhunter.domain.response.topic.ResTopicDTO;
import vn.hoidanit.jobhunter.domain.response.topic.ResUpdateTopicDTO;
import vn.hoidanit.jobhunter.repository.TopicRepository;
import vn.hoidanit.jobhunter.service.TopicService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class TopicController {
    private final TopicService topicService;

    public TopicController(TopicService topicService) {

        this.topicService = topicService;
    }

    // create a topic
    @PostMapping("/topics")
    @ApiMessage("Create successfully a new topic")
    public ResponseEntity<ResCreateTopicDTO> createTopic(@Valid @RequestBody Topic topic)
            throws IdInvalidException {
        boolean isNameExist = this.topicService.isNameExist(topic.getName());
        if (isNameExist) {
            throw new IdInvalidException(
                    "Đề tài " + topic.getName() + " đã tồn tại!");
        }
        // Topic newTopic = this.topicService.handleCreateTopic(topic);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.topicService.convertToResCreateTopicDTO(topic));
    }

    // fetch topic by id
    @GetMapping("/topics/{id}")
    @ApiMessage("fetch topic by id")
    public ResponseEntity<ResTopicDTO> getTopicById(@PathVariable("id") long id)
            throws IdInvalidException {
        Topic searchTopic = this.topicService.fetchTopicById(id);
        if (searchTopic == null) {
            throw new IdInvalidException("Topic với id = " + id + " không tồn tại!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.topicService.convertToResTopicDTO(searchTopic));
    }

    // fetch all topic
    @GetMapping("/topics")
    @ApiMessage("fetch all topics")
    public ResponseEntity<ResultPaginationDTO> getAllTopic(
            @Filter Specification<Topic> spec,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.topicService.fetchAllTopic(spec,
                pageable));
    }

    // delete topic
    @DeleteMapping("/topics/{id}")
    @ApiMessage("Delete successfully a topic")
    public ResponseEntity<String> deleteTopic(@PathVariable("id") long id) throws IdInvalidException {
        Topic currentTopic = this.topicService.fetchTopicById(id);
        if (currentTopic == null) {
            throw new IdInvalidException("Topic với id = " + id + " không tồn tại!");
        }
        this.topicService.handleDeleteTopic(id);
        return ResponseEntity.status(HttpStatus.OK).body("delete topic with id= " +
                id + " sucessfully!!");
    }

    // update topic
    @PutMapping("/topics")
    @ApiMessage("update successfully a topic")
    public ResponseEntity<ResUpdateTopicDTO> updateTopic(@RequestBody Topic topic)
            throws IdInvalidException {
        Optional<Topic> topicOptional = this.topicService.fetchTopicOptionById(topic.getId());
        // Topic upTopic = this.topicService.fetchTopicById(topic.getId());

        if (!topicOptional.isPresent()) {
            throw new IdInvalidException(
                    "Topic không tồn tại!");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.topicService.convertToResUpdateTopicDTO(topic, topicOptional.get()));
    }

    // get topic by user
    @PostMapping("/topics/by-user")
    @ApiMessage("Get list topics by user")
    public ResponseEntity<ResultPaginationDTO> fetchTopicByUser(Pageable pageable) {

        return ResponseEntity.ok().body(this.topicService.fetchTopicByUser(pageable));
    }

}
