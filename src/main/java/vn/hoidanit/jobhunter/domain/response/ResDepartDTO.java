package vn.hoidanit.jobhunter.domain.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResDepartDTO {

    private long id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private UniDepart university;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UniDepart {
        private long id;
        private String name;
    }
}
