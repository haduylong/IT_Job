package vn.hdl.itjob.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import vn.hdl.itjob.domain.User;
import vn.hdl.itjob.repository.UserRepository;
import vn.hdl.itjob.util.SecurityUtil;
import vn.hdl.itjob.util.exception.InvalidException;

@Slf4j
public class PermissionInterceptor implements HandlerInterceptor {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String pathPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        log.info(">>> pathPattern = {}", pathPattern);
        log.info(">>> http method = {}", method);
        log.info(">>> URI = {}", requestURI);

        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new InvalidException("User unauthenticated"));

        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidException("User not found"));

        if (user.getRole() != null) {
            boolean isAllowed = user.getRole().getPermissions().stream()
                    .anyMatch(permission -> pathPattern.equals(permission.getApiPath()) &&
                            method.equals(permission.getMethod()));

            if (!isAllowed) {
                throw new InvalidException("You don't have permission to access this endpoint");
            }
        } else {
            throw new InvalidException("You don't have permission to access this endpoint");
        }

        return true;
    }
}
