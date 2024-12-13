package vn.hdl.itjob.domain.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hdl.itjob.domain.Role;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespLoginDTO {
    @JsonProperty("access_token")
    private String accessToken;
    private UserLogin user;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserLogin {
        private long id;
        private String name;
        private String email;
        private Role role;
    }

    /*
     * lưu trữ những thông tin cần thiết của token
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInsideToken {
        private long id;
        private String name;
        private String email;
    }
}
