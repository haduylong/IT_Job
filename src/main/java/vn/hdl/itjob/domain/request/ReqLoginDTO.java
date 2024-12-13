package vn.hdl.itjob.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReqLoginDTO {
    @NotBlank(message = "Username can not be null")
    private String username;
    @NotBlank(message = "Password can not be null")
    private String password;
}
