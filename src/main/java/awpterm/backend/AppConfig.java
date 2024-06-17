package awpterm.backend;

import awpterm.backend.interceptor.AdminLoginCheckInterceptor;
import awpterm.backend.interceptor.LogInterceptor;
import awpterm.backend.interceptor.MemberLoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**");

        registry.addInterceptor(new MemberLoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/error",
                        "/member/login", "/member/register", "/member/kakao/login", "/member/kakao/token",
                        "/admin/**");

        registry.addInterceptor(new AdminLoginCheckInterceptor())
                .order(3)
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(Config.FRONTEND_URL)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); // 자격 증명을 허용
    }
}
