package awpterm.backend.api.kakao;

import awpterm.backend.Config;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Getter
@Setter
public class KakaoAPI {
    public static URI requestAuthorize() {
        return UriComponentsBuilder
                .fromUriString(Config.KAKAO_OAUTH_AUTHORIZE_URL)
                .queryParam("client_id", Config.KAKAO_CLIENT_KEY)
                .queryParam("redirect_uri", Config.KAKAO_REDIRECT_URL)
                .queryParam("response_type", "code")
                .encode()
                .build()
                .toUri();
    }

    public static String requestToken(String code) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", Config.KAKAO_CLIENT_KEY);
        params.add("redirect_uri", Config.KAKAO_REDIRECT_URL);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = rt.exchange(
                Config.KAKAO_OAUTH_TOKEN_URL, // https://{요청할 서버 주소}
                HttpMethod.POST, // 요청할 방식
                kakaoTokenRequest, // 요청할 때 보낼 데이터
                String.class // 요청 시 반환되는 데이터 타입
        );//Json 형태로 가져옴
        System.out.println(response);
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response.getBody());
            return jsonNode.get("access_token").asText();

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
