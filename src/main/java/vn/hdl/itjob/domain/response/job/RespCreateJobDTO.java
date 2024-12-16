package vn.hdl.itjob.domain.response.job;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hdl.itjob.util.constant.LevelEnum;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespCreateJobDTO {
    private Long id;
    private String name;

    private String location;

    private double salary;

    private int quantity;

    private LevelEnum level;

    private Instant startDate;
    private Instant endDate;
    private boolean active;

    private List<String> skills;

    private Instant createdAt;
    private String createdBy;
}
