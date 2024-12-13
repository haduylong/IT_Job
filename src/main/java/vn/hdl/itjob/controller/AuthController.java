package vn.hdl.itjob.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hdl.itjob.domain.User;
import vn.hdl.itjob.domain.request.ReqLoginDTO;
import vn.hdl.itjob.domain.response.ApiResponse;
import vn.hdl.itjob.domain.response.user.RespLoginDTO;
import vn.hdl.itjob.repository.UserRepository;
import vn.hdl.itjob.util.SecurityUtil;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
        @Value("${hdl.jwt.refresh-token-expiration-in-seconds}")
        private long refreshTokenExpiration;

        private final UserRepository userRepository;
        private final SecurityUtil securityUtil;
        private final AuthenticationManagerBuilder authenticationManagerBuilder;

        public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
                        UserRepository userRepository,
                        SecurityUtil securityUtil) {
                this.userRepository = userRepository;
                this.securityUtil = securityUtil;
                this.authenticationManagerBuilder = authenticationManagerBuilder;
        }

        @PostMapping("/auth/login")
        public ResponseEntity<ApiResponse<RespLoginDTO>> login(@Valid @RequestBody ReqLoginDTO loginDTO) {
                // đưa logic vào luồng của Spring Security
                // Nạp input gồm username/password vào Security
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                loginDTO.getUsername(), loginDTO.getPassword());

                // xác thực người dùng => cần viết hàm loadUserByUsername
                Authentication authentication = this.authenticationManagerBuilder.getObject()
                                .authenticate(authenticationToken);
                // lưu thông tin người dùng vào context
                SecurityContextHolder.getContext().setAuthentication(authentication);

                /* tạo response */
                RespLoginDTO dto = new RespLoginDTO();
                // UserLogin
                User userDb = this.userRepository.findByEmail(authentication.getName());
                RespLoginDTO.UserLogin userLogin = new RespLoginDTO.UserLogin(userDb.getId(), userDb.getName(),
                                userDb.getEmail(), userDb.getRole());
                dto.setUser(userLogin);
                // tạo access token
                String accessToken = this.securityUtil.createToken(authentication, userDb, false);
                dto.setAccessToken(accessToken);
                // tạo refresh token (lưu vào cookies)
                String refreshToken = this.securityUtil.createToken(authentication, userDb, true);
                ResponseCookie springCookie = ResponseCookie.from("refresh-token", refreshToken)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(refreshTokenExpiration)
                                // .domain("example.com")
                                .build();

                ApiResponse<RespLoginDTO> res = ApiResponse.<RespLoginDTO>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Login successful")
                                .data(dto)
                                .build();
                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                                .body(res);
        }
}
