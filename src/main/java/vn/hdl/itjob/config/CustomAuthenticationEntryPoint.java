package vn.hdl.itjob.config;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.hdl.itjob.domain.response.ApiResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();

    private final ObjectMapper mapper;

    public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        System.out.println(authException);
        // Xử lý mặc định (WWW.Authentication trong header)
        this.delegate.commence(request, response, authException);
        // Custom response body
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<Void> res = new ApiResponse<>();
        res.setError(authException.getMessage());
        res.setMessage("Token is invalid, expiration or empty ...");
        res.setStatusCode(HttpStatus.UNAUTHORIZED.value());

        mapper.writeValue(response.getWriter(), res);
    }

}
