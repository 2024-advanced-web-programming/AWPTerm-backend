package awpterm.backend.interceptor;

import awpterm.backend.etc.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;

@Slf4j
public class MemberLoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession();
        log.info("인증 체크 인터셉터 실행 {}", requestURI);


        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        if (session == null || (session.getAttribute(SessionConst.LOGIN_MEMBER) == null && session.getAttribute(SessionConst.LOGIN_ADMIN) == null)) {
            log.info("미인증 사용자 요청");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return false;
        }

        return true;
    }
}
