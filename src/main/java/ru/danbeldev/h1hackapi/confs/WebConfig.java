package ru.danbeldev.h1hackapi.confs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // разрешить все пути
                .allowedOrigins("*") // разрешить любой хост
                .allowedMethods("*") // разрешить все методы (GET, POST, PUT, DELETE и т.д.)
                .allowedHeaders("*"); // разрешить любые заголовки
    }
}
