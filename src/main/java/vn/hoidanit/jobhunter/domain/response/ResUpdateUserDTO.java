package vn.hoidanit.jobhunter.domain.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
public class ResUpdateUserDTO {
    private long id;
    private String name;
    private GenderEnum gender;
    private String address;
    private String phone;
    private int age;
    private Instant updatedAt;
    private UniversityUser university;
    private DepartmentUser department;

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

}
