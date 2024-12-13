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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hdl.itjob.domain.User;
import vn.hdl.itjob.domain.request.ReqLoginDTO;
import vn.hdl.itjob.domain.response.ApiResponse;
import vn.hdl.itjob.domain.response.user.RespCreateUserDTO;
import vn.hdl.itjob.domain.response.user.RespLoginDTO;
import vn.hdl.itjob.repository.UserRepository;
import vn.hdl.itjob.service.UserService;
import vn.hdl.itjob.util.SecurityUtil;
import vn.hdl.itjob.util.exception.InvalidException;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
        @Value("${hdl.jwt.refresh-token-expiration-in-seconds}")
        private long refreshTokenExpiration;

        private final UserRepository userRepository;
        private final SecurityUtil securityUtil;
        private final AuthenticationManagerBuilder authenticationManagerBuilder;
        private final UserService userService;
        private final PasswordEncoder passwordEncoder;

        public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
                        UserRepository userRepository,
                        SecurityUtil securityUtil,
                        UserService userService,
                        PasswordEncoder passwordEncoder) {
                this.userRepository = userRepository;
                this.securityUtil = securityUtil;
                this.authenticationManagerBuilder = authenticationManagerBuilder;
                this.userService = userService;
                this.passwordEncoder = passwordEncoder;
        }

        @PostMapping("/auth/login")
        public ResponseEntity<ApiResponse<RespLoginDTO>> login(@Valid @RequestBody ReqLoginDTO loginDTO)
                        throws InvalidException {
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
                User userDb = this.userRepository.findByEmail(authentication.getName())
                                .orElseThrow(() -> new InvalidException(
                                                "Username " + authentication.getName() + " not found"));
                RespLoginDTO.UserLogin userLogin = new RespLoginDTO.UserLogin(userDb.getId(), userDb.getName(),
                                userDb.getEmail(), userDb.getRole());
                dto.setUser(userLogin);
                // tạo access token
                String accessToken = this.securityUtil.createToken(userDb, false);
                dto.setAccessToken(accessToken);
                // tạo refresh token --> lưu vào database --> (lưu vào cookies)
                String refreshToken = this.securityUtil.createToken(userDb, true);
                userDb.setRefreshToken(refreshToken);
                this.userRepository.save(userDb);
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

        @PostMapping("/auth/logout")
        public ResponseEntity<ApiResponse<Void>> logout() throws InvalidException {
                // get current user
                String email = SecurityUtil.getCurrentUserLogin()
                                .orElseThrow(() -> new InvalidException("Access token is not valid"));

                // delete refresh token in database
                User userDb = this.userRepository.findByEmail(email)
                                .orElseThrow(() -> new InvalidException("Current user not found"));
                userDb.setRefreshToken(null);
                this.userRepository.save(userDb);
                // delete refresh token in cookie
                ResponseCookie springCookie = ResponseCookie.from("refresh-token", null)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(0)
                                // .domain("example.com")
                                .build();

                ApiResponse<Void> res = ApiResponse.<Void>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Logout successful")
                                .build();
                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                                .body(res);
        }

        @GetMapping("/auth/account")
        public ResponseEntity<ApiResponse<RespLoginDTO.UserAccount>> getAccount() throws InvalidException {
                // get current user
                String email = SecurityUtil.getCurrentUserLogin()
                                .orElseThrow(() -> new InvalidException("Access is not valid"));

                User userDb = this.userRepository.findByEmail(email)
                                .orElseThrow(() -> new InvalidException("Current user not found"));

                RespLoginDTO.UserAccount account = new RespLoginDTO.UserAccount();
                RespLoginDTO.UserLogin userLogin = new RespLoginDTO.UserLogin();
                userLogin.setId(userDb.getId());
                userLogin.setEmail(userDb.getEmail());
                userLogin.setName(userDb.getName());
                userLogin.setRole(userDb.getRole());
                account.setUser(userLogin);

                ApiResponse<RespLoginDTO.UserAccount> res = ApiResponse.<RespLoginDTO.UserAccount>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Get account successful")
                                .data(account)
                                .build();
                return ResponseEntity.ok().body(res);
        }

        @PostMapping("/auth/register")
        public ResponseEntity<ApiResponse<RespCreateUserDTO>> register(@Valid @RequestBody User reqUser)
                        throws InvalidException {
                String hashPw = this.passwordEncoder.encode(reqUser.getPassword());
                reqUser.setPassword(hashPw);
                RespCreateUserDTO dto = this.userService.handleCreateUser(reqUser);
                ApiResponse<RespCreateUserDTO> res = ApiResponse.<RespCreateUserDTO>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Resgister successful")
                                .data(dto)
                                .build();
                return ResponseEntity.ok().body(res);
        }

        @GetMapping("/auth/refresh")
        public ResponseEntity<ApiResponse<RespLoginDTO>> refreshToken(
                        @CookieValue(name = "refresh-token", defaultValue = "default-token") String reqToken)
                        throws InvalidException {
                if (reqToken.equals("default-token")) {
                        throw new InvalidException("Token not found");
                }

                Jwt decodedToken = this.securityUtil.checkValidToken(reqToken);
                String email = decodedToken.getSubject();

                /* tạo response */
                RespLoginDTO dto = new RespLoginDTO();
                // UserLogin
                User userDb = this.userRepository.findByEmail(email)
                                .orElseThrow(() -> new InvalidException(
                                                "Username " + email + " not found"));
                RespLoginDTO.UserLogin userLogin = new RespLoginDTO.UserLogin(userDb.getId(), userDb.getName(),
                                userDb.getEmail(), userDb.getRole());
                dto.setUser(userLogin);
                // tạo access token
                String accessToken = this.securityUtil.createToken(userDb, false);
                dto.setAccessToken(accessToken);
                // tạo refresh token --> lưu vào database --> (lưu vào cookies)
                String refreshToken = this.securityUtil.createToken(userDb, true);
                userDb.setRefreshToken(refreshToken);
                this.userRepository.save(userDb);
                ResponseCookie springCookie = ResponseCookie.from("refresh-token", refreshToken)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(refreshTokenExpiration)
                                // .domain("example.com")
                                .build();

                ApiResponse<RespLoginDTO> res = ApiResponse.<RespLoginDTO>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Refresh successful")
                                .data(dto)
                                .build();
                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                                .body(res);
        }
}
