package graduationwork.backend.global.oauth2.handler;

import graduationwork.backend.domain.user.repository.UserRepository;
import graduationwork.backend.global.config.jwt.service.JwtService;
import graduationwork.backend.global.oauth2.CustomOAuth2User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            log.info(oAuth2User.getAttributes().toString());

            String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
            String refreshToken = jwtService.createRefreshToken();

            String redirectUrl = "http://localhost:8080/redirect/" + accessToken + "/" + refreshToken;
            response.sendRedirect(redirectUrl);

            userRepository.findByEmail(oAuth2User.getEmail())
                    .ifPresent(user -> {
                        user.updateRefreshToken(refreshToken);
                        userRepository.saveAndFlush(user);
                    });

        } catch (Exception e) {
            log.info(e.getMessage());
            throw e;
        }


    }


}
