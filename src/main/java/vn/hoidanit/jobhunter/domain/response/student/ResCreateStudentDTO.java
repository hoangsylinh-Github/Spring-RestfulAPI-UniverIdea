package vn.hoidanit.jobhunter.domain.response.student;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateStudentDTO {
    private long id;
    private String name;
    private String studentId;
    private String classroom;
    private String email;
    private String phone;
    private Instant createdAt;
    private StudentUni university;
    private StudentDepart department;

    @Getter
    @Setter
    public static class StudentUni {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    public static class StudentDepart {
        private long id;
        private String name;
    }
}
