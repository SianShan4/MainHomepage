package welcomecording.mainHomepage.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import welcomecording.mainHomepage.model.KakaoProfile;
import welcomecording.mainHomepage.model.OAuthToken;
import welcomecording.mainHomepage.model.User;

import java.util.UUID;

// 카카오 서비스
// 토큰을 받았다는 가정.
@Service
public class KakaoService {

    @Autowired
    UserService userService;

    public ResponseEntity<String> codeToToken(String code) {
        System.out.println("----------");
        System.out.println("code to token : method start");

        RestTemplate rt = new RestTemplate();

        HttpHeaders Headers = new HttpHeaders();
        Headers.add("Contect-type","application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "e0860e978d4368033b5c758cfd4b537e");
        params.add("redirect_uri", "http://localhost:8080/auth/kakao/callback");
        params.add("code", code);
        params.add("client_secret", "f57qbtkJ6UwOBo4SeT5BlhuGa2uCgTow");

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, Headers);
        System.out.println("kakaoTokenRequest : Rapping success");

        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );
        System.out.println("kakaoTokenRequest : sending success");
        System.out.println("kakao response body data : " + response.getBody());

        return response;
    }

    public OAuthToken tokenToJava(ResponseEntity<String> response) {
        System.out.println("----------");
        System.out.println("data to Json : method start");

        ObjectMapper obMapper = new ObjectMapper();

        OAuthToken oauthToken = null;
        try {
            oauthToken = obMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("kakao access token : "+oauthToken.getAccess_token());
        System.out.println("kakao scope of token : "+oauthToken.getScope());

        return oauthToken;
    }

    public ResponseEntity<String> getKakaoData(OAuthToken oAuthToken) {
        System.out.println("----------");
        System.out.println("get kakao data : method start");

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+oAuthToken);
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                new HttpEntity<>(headers);
        System.out.println("kakaoProfileRequest : Rapping success");

        ResponseEntity<String> response = rt.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    kakaoProfileRequest,
                    String.class
            );

        System.out.println("kakaoTokenRequest : Data Reqeust sending success");
        System.out.println("Kakao Data (Json data) : "+response.getBody());

        return response;
    }

    public KakaoProfile kakaoDataToJava(ResponseEntity<String> response) {
        System.out.println("----------");
        System.out.println("kakao data to Json : method start");

        ObjectMapper obMapper2 = new ObjectMapper();

        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = obMapper2.readValue(response.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("kakao profile Json data to Java : success");
        System.out.println("kakao id : "+kakaoProfile.getId());
        System.out.println("kakao email : "+kakaoProfile.getKakao_account().getEmail());
        System.out.println("kakao nickname (getProperties) : "+kakaoProfile.getProperties().getNickname());
        System.out.println("kakao nickname (getProfile): "+kakaoProfile.getKakao_account().getProfile().getNickname());

        return kakaoProfile;
    }

    public User createKakaoUser(KakaoProfile kakaoProfile) {
        System.out.println("----------");
        System.out.println("create kakao user : method start");

        UUID dummyPassword = UUID.randomUUID();

        User kakaoUser = User.builder()
                .username(kakaoProfile.getKakao_account().getProfile().getNickname())
                .password(dummyPassword.toString())
                .email(kakaoProfile.getKakao_account().getEmail())
                .picture(kakaoProfile.getProperties().getProfile_image())
                .build();
        userService.join(kakaoUser);

        return kakaoUser;
    }

    public ResponseEntity<String> updateToken(OAuthToken oAuthToken) {
        RestTemplate rt_refresh = new RestTemplate();
        HttpHeaders headers_refresh = new HttpHeaders();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        headers_refresh.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        params.add("grant_type", "refresh_token");
        params.add("client_id", "e0860e978d4368033b5c758cfd4b537e");
        params.add("refresh_token", oAuthToken.getRefresh_token());
        HttpEntity<MultiValueMap<String, String>> getRefreshTokenRequest = new HttpEntity<>(params,headers_refresh);
        ResponseEntity<String> response_refresh = rt_refresh.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                getRefreshTokenRequest,
                String.class
        );
        return response_refresh;
    }

}
