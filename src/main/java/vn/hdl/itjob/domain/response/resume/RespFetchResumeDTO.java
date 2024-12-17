package vn.hdl.itjob.domain.response.resume;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hdl.itjob.util.constant.ResumeStatusEnum;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespFetchResumeDTO {
    private Long id;
    private String email;
    private String url;
    private ResumeStatusEnum status;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    private String companyName;
    private JobOfResume job;
    private UserOfResume user;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JobOfResume {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserOfResume {
        private Long id;
        private String name;
    }
}
