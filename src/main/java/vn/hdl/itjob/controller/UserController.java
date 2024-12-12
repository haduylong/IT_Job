package vn.hdl.itjob.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hdl.itjob.domain.User;
import vn.hdl.itjob.domain.response.ApiResponse;
import vn.hdl.itjob.domain.response.user.RespCreateUserDTO;
import vn.hdl.itjob.service.UserService;
import vn.hdl.itjob.util.exception.AppException;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<RespCreateUserDTO>> createUser(@Valid @RequestBody User reqUser)
            throws AppException {
        RespCreateUserDTO dto = this.userService.handleCreateUser(reqUser);
        ApiResponse<RespCreateUserDTO> res = ApiResponse.<RespCreateUserDTO>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Create user successful")
                .data(dto)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
