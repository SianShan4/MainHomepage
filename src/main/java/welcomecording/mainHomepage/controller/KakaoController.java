package welcomecording.mainHomepage.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import welcomecording.mainHomepage.model.KakaoProfile;
import welcomecording.mainHomepage.model.OAuthToken;
import welcomecording.mainHomepage.model.User;
import welcomecording.mainHomepage.service.CustomOAuth2UserService;
import welcomecording.mainHomepage.service.KakaoService;
import welcomecording.mainHomepage.service.UserService;

import java.io.IOException;

@Controller
public class KakaoController {

    @Autowired
    private UserService userService;
    @Autowired
    KakaoService kakaoService;

    // 카카오 로그인
    // code를 통해 토큰을 가져오고, 토큰을 통해 데이터를 Json 형식으로 가져오고, Json을 java 오브젝트로 변환하고,
    @GetMapping("/auth/kakao/callback")
    public String kakaoLogin(@RequestParam String code) throws IOException {
        System.out.println("kakao login controller : method start");

        OAuthToken oAuthToken = kakaoService.tokenToJava(kakaoService.codeToToken(code));
        KakaoProfile kakaoProfile = kakaoService.kakaoDataToJava(kakaoService.getKakaoData(oAuthToken));
        User kakaoUser = kakaoService.createKakaoUser(kakaoProfile);



        return "home";
    }

    @GetMapping("user/updateForm")
    public String updateForm() {
        return "updateForm";
    }
}
