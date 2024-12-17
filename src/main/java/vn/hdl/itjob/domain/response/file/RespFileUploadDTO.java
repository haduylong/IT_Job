package vn.hdl.itjob.domain.response.file;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespFileUploadDTO {
    private String fileName;
    private Instant uploadedAt;
}
