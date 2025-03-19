package vn.hoidanit.jobhunter.domain.response.user;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO {
    private long id;
    private String name;
    private String email;
    private int age;
    private GenderEnum gender;
    private String address;
    private String phone;
    private Instant createdAt;
    private Instant updatedAt;
    private UniversityUser university;
    private DepartmentUser department;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UniversityUser {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DepartmentUser {
        private long id;
        private String name;
    }
}
