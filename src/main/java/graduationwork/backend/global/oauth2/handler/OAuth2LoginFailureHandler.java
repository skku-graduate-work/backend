package graduationwork.backend.global.oauth2.handler;

import graduationwork.backend.global.error.exception.ErrorCode;
import graduationwork.backend.global.error.exception.UnauthorizedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.info("소셜 로그인에 실패했습니다. 에러 메시지 : {}", exception.getMessage());
        throw new UnauthorizedException(ErrorCode.FAILT_SOCIAL_LOGIN);
    }
}
