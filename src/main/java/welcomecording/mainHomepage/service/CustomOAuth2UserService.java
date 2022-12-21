package welcomecording.mainHomepage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import welcomecording.mainHomepage.model.OAuthAttributes;
import welcomecording.mainHomepage.model.SessionUser;
import welcomecording.mainHomepage.model.User;
import welcomecording.mainHomepage.repository.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();

        OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = oAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // oAuth2Uesr : service에서 oauth request를 받고 loadUser() 실행하여 뽑아낸 oauth정보객체.
        // registrationId는 oauth provider를 구분하기 위해서 받음.
        // userNameAttributeName은 뭔지 모르겠음. (?) ofKakao에서 사용함.
        // oauth2 인증 정보를 바탕으로 user 객체 생성 및 수정. saveOrUpdate() 사용.
        // 리턴되는건 각 provider에 맞는 oauthAttributes 객체. 내 경우엔 kakao
        // 근데 여기서 정보 code로 정보 뽑아오는 메서드 만들어야함. -> KakaoService.class 구현 완. User kakaoUser 반환.
        // 이걸 어디다 붙이지.
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user));

        System.out.println(attributes.getAttributes());
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
                , attributes.getAttributes()
                , attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getUsername(), attributes.getEmail(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
