package vn.hoidanit.jobhunter.domain.response.user;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private int age;
    private GenderEnum gender;
    private String address;
    private String phone;
    private Instant createdAt;
    private UniversityUser university;
    private DepartmentUser department;
    private RoleUser role;

    @Getter
    @Setter
    public static class UniversityUser {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    public static class DepartmentUser {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    public static class RoleUser {
        private long id;
        private String name;
    }
}
