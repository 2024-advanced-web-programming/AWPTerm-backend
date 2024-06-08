package awpterm.backend.interceptor;

import awpterm.backend.etc.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();
        request.setAttribute(SessionConst.LOG_ID, uuid);
        log.info("요청 수신: [{}{}{}]",uuid, requestURI, handler);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String requestURI = request.getRequestURI();
        String uuid = (String) request.getAttribute(SessionConst.LOG_ID);
        log.info("응답 반환: [{}][{}][{}]", uuid, requestURI, handler);

        if(ex!=null){
            log.error("예외 발생", ex);
        }
    }
}
