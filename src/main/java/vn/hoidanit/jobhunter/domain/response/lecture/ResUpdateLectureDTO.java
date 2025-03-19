package vn.hoidanit.jobhunter.domain.response.lecture;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateLectureDTO {
    private long id;
    private String name;
    private String email;
    private String phone;
    private Instant updatedAt;
    private LectureUni university;
    private LectureDepart department;

    @Getter
    @Setter
    public static class LectureUni {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    public static class LectureDepart {
        private long id;
        private String name;
    }
}
