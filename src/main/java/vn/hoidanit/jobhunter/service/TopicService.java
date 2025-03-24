package vn.hoidanit.jobhunter.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;

import jakarta.persistence.criteria.Join;
import vn.hoidanit.jobhunter.domain.Department;
import vn.hoidanit.jobhunter.domain.Topic;
import vn.hoidanit.jobhunter.domain.University;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.topic.ResCreateTopicDTO;
import vn.hoidanit.jobhunter.domain.response.topic.ResTopicDTO;
import vn.hoidanit.jobhunter.domain.response.topic.ResUpdateTopicDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResUserDTO;
import vn.hoidanit.jobhunter.repository.TopicRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.SecurityUtil;

@Service
public class TopicService {
    @Autowired
    FilterBuilder fb;

    @Autowired
    private FilterParser filterParser;

    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;

    private final TopicRepository topicRepository;
    private final UniversityService universityService;
    private final DepartmentService departmentService;
    private final UserRepository userRepository;

    private final UserService userService;

    public TopicService(TopicRepository topicRepository,
            UniversityService universityService,
            DepartmentService departmentService,
            UserService userService,
            UserRepository userRepository) {
        this.topicRepository = topicRepository;
        this.universityService = universityService;
        this.departmentService = departmentService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public boolean isNameExist(String name) {
        return this.topicRepository.existsByName(name);
    }

    public Topic fetchTopicById(long id) {
        Optional<Topic> topicOptional = this.topicRepository.findById(id);
        if (topicOptional.isPresent()) {
            return topicOptional.get();
        }
        return null;
    }

    public Optional<Topic> fetchTopicOptionById(long id) {
        return this.topicRepository.findById(id);
    }

    public ResCreateTopicDTO convertToResCreateTopicDTO(Topic topic) {
        ResCreateTopicDTO resTopic = new ResCreateTopicDTO();
        // ResCreateTopicDTO.TopicUser user = new ResCreateTopicDTO.TopicUser();
        ResCreateTopicDTO.TopicUni uni = new ResCreateTopicDTO.TopicUni();
        ResCreateTopicDTO.TopicDepart depart = new ResCreateTopicDTO.TopicDepart();

        // check university
        if (topic.getUniversity() != null) {
            Optional<University> uniOptional = this.universityService.findById(topic.getUniversity().getId());
            topic.setUniversity(uniOptional.isPresent() ? uniOptional.get() : null);
        }

        // check department
        if (topic.getDepartment() != null) {
            Optional<Department> departOptional = this.departmentService.findById(topic.getDepartment().getId());
            topic.setDepartment(departOptional.isPresent() ? departOptional.get() : null);
        }

        // check user
        if (topic.getUser() != null) {
            List<Long> reqUser = topic.getUser()
                    .stream().map(x -> x.getId()).collect(Collectors.toList());

            List<User> dbUser = this.userRepository.findByIdIn(reqUser);
            topic.setUser(dbUser);
        }

        Topic tempTopic = this.topicRepository.save(topic);
        tempTopic = this.topicRepository.findById(tempTopic.getId()).orElse(null);

        if (tempTopic.getUser() != null) {
            List<String> users = tempTopic.getUser()
                    .stream().map(item -> item.getEmail())
                    .collect(Collectors.toList());
            resTopic.setUser(users);
        }

        if (tempTopic.getUniversity() != null) {
            uni.setId(tempTopic.getUniversity().getId());
            uni.setName(tempTopic.getUniversity().getName());
            resTopic.setUniversity(uni);
        }

        if (tempTopic.getDepartment() != null) {
            depart.setId(tempTopic.getDepartment().getId());
            depart.setName(tempTopic.getDepartment().getName());
            resTopic.setDepartment(depart);
        }

        resTopic.setId(tempTopic.getId());
        resTopic.setName(tempTopic.getName());
        resTopic.setDescription(tempTopic.getDescription());
        resTopic.setStatus(tempTopic.getStatus());
        resTopic.setYear(tempTopic.getYear());
        resTopic.setUrl(tempTopic.getUrl());
        resTopic.setLecture(tempTopic.getLecture());
        resTopic.setCreatedAt(tempTopic.getCreatedAt());
        resTopic.setCreatedBy(tempTopic.getCreatedBy());
        return resTopic;
    }

    public ResTopicDTO convertToResTopicDTO(Topic topic) {
        ResTopicDTO resTopic = new ResTopicDTO();
        // ResTopicDTO.TopicUser user = new ResTopicDTO.TopicUser();
        ResTopicDTO.TopicUni uni = new ResTopicDTO.TopicUni();
        ResTopicDTO.TopicDepart depart = new ResTopicDTO.TopicDepart();

        resTopic.setId(topic.getId());
        resTopic.setName(topic.getName());
        resTopic.setDescription(topic.getDescription());
        resTopic.setStatus(topic.getStatus());
        resTopic.setYear(topic.getYear());
        resTopic.setUrl(topic.getUrl());
        resTopic.setLecture(topic.getLecture());
        resTopic.setCreatedAt(topic.getCreatedAt());
        resTopic.setUpdatedAt(topic.getUpdatedAt());

        // check user
        if (topic.getUser() != null) {
            List<Long> reqUser = topic.getUser()
                    .stream().map(x -> x.getId()).collect(Collectors.toList());

            List<User> dbUser = this.userRepository.findByIdIn(reqUser);
            topic.setUser(dbUser);
        }

        Topic tempTopic = this.topicRepository.save(topic);
        tempTopic = this.topicRepository.findById(tempTopic.getId()).orElse(null);

        if (tempTopic.getUser() != null) {
            List<String> users = tempTopic.getUser()
                    .stream().map(item -> item.getEmail())
                    .collect(Collectors.toList());
            resTopic.setUser(users);
        }

        if (topic.getUniversity() != null) {
            uni.setId(topic.getUniversity().getId());
            uni.setName(topic.getUniversity().getName());
            resTopic.setUniversity(uni);
        }

        if (topic.getDepartment() != null) {
            depart.setId(topic.getDepartment().getId());
            depart.setName(topic.getDepartment().getName());
            resTopic.setDepartment(depart);
        }
        return resTopic;
    }

    public ResultPaginationDTO fetchAllTopic(Specification<Topic> spec, Pageable pageable) {
        Page<Topic> pageTopic = this.topicRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageTopic.getTotalPages());
        mt.setTotal(pageTopic.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResTopicDTO> listTopic = pageTopic.getContent()
                .stream().map(item -> new ResTopicDTO(
                        item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getStatus(),
                        item.getYear(),
                        item.getUrl(),
                        item.getLecture(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        item.getUser() != null
                                ? item.getUser().stream().map(user -> user.getEmail()).collect(Collectors.toList())
                                : Collections.emptyList(),
                        new ResTopicDTO.TopicUni(
                                item.getUniversity() != null ? item.getUniversity().getId() : 0,
                                item.getUniversity() != null ? item.getUniversity().getName() : null),
                        new ResTopicDTO.TopicDepart(
                                item.getDepartment() != null ? item.getDepartment().getId() : 0,
                                item.getDepartment() != null ? item.getDepartment().getName() : null)))
                .collect(Collectors.toList());

        rs.setResult(listTopic);

        return rs;
    }

    public void handleDeleteTopic(long id) {
        this.topicRepository.deleteById(id);
    }

    // public Topic handleUpdateTopic(Topic reqTopic) {
    // Topic currenTopic = this.fetchTopicById(reqTopic.getId());
    // if (currenTopic != null) {
    // currenTopic.setName(reqTopic.getName());
    // currenTopic.setDescription(reqTopic.getDescription());
    // currenTopic.setStatus(reqTopic.getStatus());
    // currenTopic.setYear(reqTopic.getYear());
    // currenTopic.setUrl(reqTopic.getUrl());
    // currenTopic.setLecture(reqTopic.getLecture());

    // // check user
    // if (reqTopic.getUser() != null) {
    // List<Long> reqUser = reqTopic.getUser()
    // .stream().map(x -> x.getId()).collect(Collectors.toList());

    // List<User> dbUser = this.userRepository.findByIdIn(reqUser);
    // reqTopic.setUser(dbUser);
    // }

    // // check university
    // if (reqTopic.getUniversity() != null) {
    // Optional<University> uniOptional =
    // this.universityService.findById(reqTopic.getUniversity().getId());
    // reqTopic.setUniversity(uniOptional.isPresent() ? uniOptional.get() : null);
    // currenTopic.setUniversity(reqTopic.getUniversity());
    // }

    // // check department
    // if (reqTopic.getDepartment() != null) {
    // Optional<Department> departOptional =
    // this.departmentService.findById(reqTopic.getDepartment().getId());
    // reqTopic.setDepartment(departOptional.isPresent() ? departOptional.get() :
    // null);
    // currenTopic.setDepartment(reqTopic.getDepartment());
    // }

    // currenTopic = this.topicRepository.save(currenTopic);
    // }
    // return currenTopic;
    // }

    public ResUpdateTopicDTO convertToResUpdateTopicDTO(Topic topic, Topic topicInDB) {
        ResUpdateTopicDTO resTopic = new ResUpdateTopicDTO();
        ResUpdateTopicDTO.TopicUni uni = new ResUpdateTopicDTO.TopicUni();
        ResUpdateTopicDTO.TopicDepart depart = new ResUpdateTopicDTO.TopicDepart();

        // check university
        if (topic.getUniversity() != null) {
            Optional<University> uniOptional = this.universityService.findById(topic.getUniversity().getId());
            topic.setUniversity(uniOptional.isPresent() ? uniOptional.get() : null);
        }

        // check department
        if (topic.getDepartment() != null) {
            Optional<Department> departOptional = this.departmentService.findById(topic.getDepartment().getId());
            topic.setDepartment(departOptional.isPresent() ? departOptional.get() : null);
        }

        // check user
        if (topic.getUser() != null) {
            List<Long> reqUser = topic.getUser()
                    .stream().map(x -> x.getId()).collect(Collectors.toList());

            List<User> dbUser = this.userRepository.findByIdIn(reqUser);
            topicInDB.setUser(dbUser);
        }

        topicInDB.setId(topic.getId());
        topicInDB.setName(topic.getName());
        topicInDB.setDescription(topic.getDescription());
        topicInDB.setStatus(topic.getStatus());
        topicInDB.setYear(topic.getYear());
        topicInDB.setUrl(topic.getUrl());
        topicInDB.setLecture(topic.getLecture());
        topicInDB.setUpdatedAt(topic.getUpdatedAt());

        Topic currentTopic = this.topicRepository.save(topic);
        currentTopic = this.topicRepository.findById(currentTopic.getId()).orElse(null);

        resTopic.setId(currentTopic.getId());
        resTopic.setName(currentTopic.getName());
        resTopic.setDescription(currentTopic.getDescription());
        resTopic.setStatus(currentTopic.getStatus());
        resTopic.setYear(currentTopic.getYear());
        resTopic.setUrl(currentTopic.getUrl());
        resTopic.setLecture(currentTopic.getLecture());
        resTopic.setUpdatedAt(currentTopic.getUpdatedAt());

        if (currentTopic.getUser() != null) {
            List<String> users = currentTopic.getUser()
                    .stream().map(item -> item.getEmail())
                    .collect(Collectors.toList());
            resTopic.setUser(users);
        }

        if (currentTopic.getUniversity() != null) {
            uni.setId(currentTopic.getUniversity().getId());
            uni.setName(currentTopic.getUniversity().getName());
            resTopic.setUniversity(uni);
        }

        if (currentTopic.getDepartment() != null) {
            depart.setId(currentTopic.getDepartment().getId());
            depart.setName(currentTopic.getDepartment().getName());
            resTopic.setDepartment(depart);
        }
        return resTopic;
    }

    public ResultPaginationDTO fetchTopicByUser(Pageable pageable) {
        // query builder
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        FilterNode node = filterParser.parse("email='" + email + "'");
        // FilterSpecification<Topic> spec = filterSpecificationConverter.convert(node);
        Specification<Topic> spec = (root, query, criteriaBuilder) -> {
            Join<Topic, User> userJoin = root.join("user");
            return criteriaBuilder.equal(userJoin.get("email"), email);
        };
        Page<Topic> pageTopic = this.topicRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageTopic.getTotalPages());
        mt.setTotal(pageTopic.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResTopicDTO> listTopic = pageTopic.getContent()
                .stream().map(item -> this.convertToResTopicDTO(item))
                .collect(Collectors.toList());

        rs.setResult(listTopic);

        return rs;
    }

}
