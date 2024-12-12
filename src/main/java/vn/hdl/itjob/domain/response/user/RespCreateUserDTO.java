package vn.hdl.itjob.domain.response.user;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hdl.itjob.util.constant.GenderEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RespCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private int age;
    private GenderEnum gender;
    private String address;
    private Instant createdAt;
    private Instant updatedAt;
    private CompanyOfUser company;
    private RoleOfUser role;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompanyOfUser {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoleOfUser {
        private long id;
        private String name;
    }
}
