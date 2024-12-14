package vn.hdl.itjob.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hdl.itjob.domain.User;
import vn.hdl.itjob.domain.response.ApiResponse;
import vn.hdl.itjob.domain.response.ResultPaginationDTO;
import vn.hdl.itjob.domain.response.user.RespCreateUserDTO;
import vn.hdl.itjob.domain.response.user.RespUpdateUserDTO;
import vn.hdl.itjob.domain.response.user.RespUserDTO;
import vn.hdl.itjob.service.UserService;
import vn.hdl.itjob.util.exception.InvalidException;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<RespCreateUserDTO>> createUser(@Valid @RequestBody User reqUser)
            throws InvalidException {
        // encode password
        String hashPassword = this.passwordEncoder.encode(reqUser.getPassword());
        reqUser.setPassword(hashPassword);

        RespCreateUserDTO dto = this.userService.handleCreateUser(reqUser);
        ApiResponse<RespCreateUserDTO> res = ApiResponse.<RespCreateUserDTO>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Create user successful")
                .data(dto)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/users")
    public ResponseEntity<ApiResponse<RespUpdateUserDTO>> updateUser(@RequestBody User reqUser)
            throws InvalidException {
        RespUpdateUserDTO dto = this.userService.handleUpdateUser(reqUser);
        ApiResponse<RespUpdateUserDTO> res = ApiResponse.<RespUpdateUserDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Update user successful")
                .data(dto)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<RespUserDTO>> getUser(@PathVariable("id") Long id)
            throws InvalidException {
        RespUserDTO dto = this.userService.handleGetUser(id);
        ApiResponse<RespUserDTO> res = ApiResponse.<RespUserDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get user successful")
                .data(dto)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getAllUser(
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        ResultPaginationDTO dto = this.userService.handleGetAllUser(page, size);
        ApiResponse<ResultPaginationDTO> res = ApiResponse.<ResultPaginationDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get all user successful")
                .data(dto)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@RequestParam("id") Long id) {
        this.userService.handleDeleteUser(id);
        ApiResponse<Void> res = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Delete user successful")
                .build();
        return ResponseEntity.ok().body(res);
    }
}
