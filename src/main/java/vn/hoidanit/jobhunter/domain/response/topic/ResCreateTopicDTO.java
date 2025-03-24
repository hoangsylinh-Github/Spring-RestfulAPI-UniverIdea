package vn.hoidanit.jobhunter.domain.response.topic;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.StatusEnum;

@Getter
@Setter
public class ResCreateTopicDTO {
    private long id;
    private String name;
    private String description;
    private StatusEnum status;
    private int year;
    private String url;
    private String lecture;
    private Instant createdAt;
    private String createdBy;
    // private TopicUser user;
    private List<String> user;
    private TopicUni university;
    private TopicDepart department;

    // @Getter
    // @Setter
    // public static class TopicUser {
    // private long id;
    // private String name;
    // }

    @Getter
    @Setter
    public static class TopicUni {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    public static class TopicDepart {
        private long id;
        private String name;
    }
}
