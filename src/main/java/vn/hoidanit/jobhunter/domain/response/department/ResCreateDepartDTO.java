package vn.hoidanit.jobhunter.domain.response.department;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateDepartDTO {
    private long id;
    private String name;
    private String description;
    private Instant createdAt;
    private UniDepart university;

    @Getter
    @Setter
    public static class UniDepart {
        private long id;
        private String name;
    }
}
