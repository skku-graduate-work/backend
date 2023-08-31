package graduationwork.backend.global.config.jwt.filter;

import graduationwork.backend.global.error.exception.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtExceptionFilter: Handling JWT exceptions...");
        try {
            filterChain.doFilter(request, response);
        } catch (UnauthorizedException ex) {
            logger.info(ex);
            request.setAttribute("Exception", ex);
            filterChain.doFilter(request, response);
        }


    }

}
