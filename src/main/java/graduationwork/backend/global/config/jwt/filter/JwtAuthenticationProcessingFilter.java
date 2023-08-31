package graduationwork.backend.global.config.jwt.filter;


import graduationwork.backend.domain.user.domain.User;
import graduationwork.backend.domain.user.repository.UserRepository;
import graduationwork.backend.global.config.jwt.service.JwtService;
import graduationwork.backend.global.config.jwt.util.PasswordUtil;
import graduationwork.backend.global.error.exception.ErrorCode;
import graduationwork.backend.global.error.exception.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.core.userdetails.User.builder;


@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final List<RequestMatcher> specialUrlMatchers;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        for (RequestMatcher requestMatcher : specialUrlMatchers) {
            if (requestMatcher.matches(request)) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        log.info("JwtAuthenticationProcessingFilter: Token validation and authentication...");
        log.info(request.getRequestURI());

        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        if (refreshToken != null) {
            log.info("리프레시 토큰 헤더에 존재 -> 리프레시 토큰 DB의 리프레시 토큰과 일치 여부 판단 후 일치 시 accessToken 도 재발급");
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }

        if (refreshToken == null) {
            log.info("리프레시 토큰 헤더에 존재 안함 -> 엑세스 토큰 검사");
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }

    }


    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        userRepository.findByRefreshToken(refreshToken)
                .ifPresentOrElse(
                        user -> {
                            String reIssuedRefreshToken = reIssueRefreshToken(user);
                            try {
                                jwtService.sendAccessTokenAndRefreshToken(response, jwtService.createAccessToken(user.getEmail()), reIssuedRefreshToken);
                            } catch (IOException e) {
                                throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
                            }
                        }, () -> {
                            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN_VALUE);
                        }
                );

    }

    private String reIssueRefreshToken(User user) {
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        user.updateRefreshToken(reIssuedRefreshToken);
        userRepository.saveAndFlush(user);
        return reIssuedRefreshToken;

    }

    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("checkAccessTokenAndAuthentication() 호출");
        Optional<String> accessTokenOptional = jwtService.extractAccessToken(request);
        if (accessTokenOptional.isEmpty()) {
            log.info("엑세스 토큰 헤더에 없음");
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN_VALUE);
        }
        String accessToken = accessTokenOptional.get();
        log.info("엑세스 토큰 헤더에 있음");

        if (!jwtService.isTokenValid(accessToken)) {
            log.info("유효하지 않은 엑세스 토큰임");
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN_VALUE);
        }

        Optional<String> emailOptional = jwtService.extractEmail(accessToken);

        if (emailOptional.isEmpty()) {
            log.info("유효하지 않은 엑세스 토큰임 (Failed to extract email)");
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN_VALUE);
        }

        String email = emailOptional.get();
        log.info("유효한 엑세스 토큰임 (해당 사용자 존재).{}",email);
        userRepository.findByEmail(email).ifPresent(this::saveAuthentication);

        response.setStatus(HttpServletResponse.SC_OK);
        filterChain.doFilter(request, response);
    }


    private void saveAuthentication(User myUser) {
        String password = myUser.getPassword();
        if (password == null) { // 소셜 로그인 유저의 비밀번호 임의로 설정 하여 소셜 로그인 유저도 인증 되도록 설정
            password = PasswordUtil.generateRandomPassword();
        }

        UserDetails userDetailsUser = builder()
                .username(myUser.getEmail())
                .password(password)
                .roles("USER")
                .build();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
