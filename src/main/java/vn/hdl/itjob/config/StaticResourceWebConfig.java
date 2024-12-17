package vn.hdl.itjob.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceWebConfig implements WebMvcConfigurer {
    @Value("${hdl.upload-dir.base-uri-path}")
    private String baseUri;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/storage/**")
                .addResourceLocations(baseUri);
        // .setCacheControl(CacheControl.maxAge(Duration.ofDays(365)));
    }
}
