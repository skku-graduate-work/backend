package graduationwork.backend.global.oauth2.service;

import graduationwork.backend.domain.user.domain.User;
import graduationwork.backend.domain.user.repository.UserRepository;
import graduationwork.backend.global.oauth2.dto.CustomOAuth2User;
import graduationwork.backend.global.oauth2.dto.OAuthAttributes;
import graduationwork.backend.global.oauth2.dto.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);
        User createdUser = getUser(extractAttributes);
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("user")),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdUser.getEmail()
        );
    }

    private User getUser(OAuthAttributes attributes) {
        User findUser = userRepository.findByEmail(attributes.getOAuth2UserInfo().getEmail()).orElse(null);
        if (findUser == null) {
            return saveUser(attributes);
        }
        return findUser;
    }

    private User saveUser(OAuthAttributes attributes) {
        //이메일, 이름, 프로필 사진 설정
        User createdUser = attributes.toEntity(attributes.getOAuth2UserInfo());
        return userRepository.save(createdUser);
    }

    private SocialType getSocialType(String registrationId) {
        if (NAVER.equals(registrationId)) {
            return SocialType.NAVER;
        }
        if (KAKAO.equals(registrationId)) {
            return SocialType.KAKAO;
        }
        return SocialType.GOOGLE;
    }
}
