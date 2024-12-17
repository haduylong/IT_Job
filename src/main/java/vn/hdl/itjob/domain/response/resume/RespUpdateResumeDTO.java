package vn.hdl.itjob.domain.response.resume;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RespUpdateResumeDTO {
    private Instant createdAt;
    private String createdBy;
}
