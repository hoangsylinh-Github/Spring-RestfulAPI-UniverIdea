package vn.hoidanit.jobhunter.domain.response.topic;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.StatusEnum;

@Getter
@Setter
@NoArgsConstructor
public class ResTopicDTO {
    public ResTopicDTO(long id, String name, String description, StatusEnum status, int year, String reward, String url,
            String lecture, Instant createdAt, Instant updatedAt, List<String> user,
            TopicUni university, TopicDepart department) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.year = year;
        this.url = url;
        this.lecture = lecture;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user;
        this.university = university;
        this.department = department;
    }

    private long id;
    private String name;
    private String description;
    private StatusEnum status;
    private int year;
    private String reward;
    private String url;
    private String students;
    private String lecture;
    private Instant createdAt;
    private Instant updatedAt;
    private List<String> user;
    private TopicUni university;
    private TopicDepart department;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TopicUni {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TopicDepart {
        private long id;
        private String name;
    }
}
