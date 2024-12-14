package vn.hdl.itjob.domain.response.user;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hdl.itjob.util.constant.GenderEnum;

@Getter
@Setter
public class RespUpdateUserDTO {
    private long id;
    private String name;
    private int age;
    private GenderEnum gender;
    private String address;
    private Instant updatedAt;
    private CompanyOfUser company;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompanyOfUser {
        private long id;
        private String name;
    }
}
