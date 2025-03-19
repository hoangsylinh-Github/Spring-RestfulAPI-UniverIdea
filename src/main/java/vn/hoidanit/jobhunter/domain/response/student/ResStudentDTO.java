package vn.hoidanit.jobhunter.domain.response.student;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResStudentDTO {
    private long id;
    private String name;
    private String studentId;
    private String classroom;
    private String email;
    private String phone;
    private Instant createdAt;
    private Instant updatedAt;
    private StudentUni university;
    private StudentDepart department;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StudentDepart {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StudentUni {
        private long id;
        private String name;
    }

}
