package vn.hoidanit.jobhunter.domain.response.lecture;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResLectureDTO {
    private long id;
    private String name;
    private String email;
    private String phone;
    private Instant createdAt;
    private Instant updatedAt;
    private LectureUni university;
    private LectureDepart department;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LectureUni {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LectureDepart {
        private long id;
        private String name;
    }
}
