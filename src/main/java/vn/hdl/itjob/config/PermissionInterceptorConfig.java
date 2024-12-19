package vn.hdl.itjob.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PermissionInterceptorConfig implements WebMvcConfigurer {
    @Bean
    PermissionInterceptor getPermissionInterceptor() {
        return new PermissionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] whiteList = {
                "/api/v1/auth/**",
                "/api/v1/public/**",
                "/storage/**",
                "/api/v1/companies/**",
                "/api/v1/skills/**",
                "/api/v1/jobs/**",
                "/api/v1/resumes/**",
                "/api/v1/files"
        };

        registry.addInterceptor(getPermissionInterceptor())
                .excludePathPatterns(whiteList);
    }
}