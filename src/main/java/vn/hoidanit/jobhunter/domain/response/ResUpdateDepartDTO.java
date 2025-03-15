package vn.hoidanit.jobhunter.domain.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateDepartDTO {
    private long id;
    private String name;
    private String description;
    private Instant updatedAt;
    private UniDepart university;

    @Getter
    @Setter
    public static class UniDepart {
        private long id;
        private String name;
    }
}
