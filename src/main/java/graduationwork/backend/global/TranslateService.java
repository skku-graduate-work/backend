package graduationwork.backend.global;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class TranslateService {
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String XNaverClientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String XNaverClientSecret;

    public String translateText(String sourceLanguage, String targetLanguage, String text) {
        final String url = "https://openapi.naver.com/v1/papago/n2mt";
        String translatedText = null;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        log.info(XNaverClientId, XNaverClientSecret);
        httpHeaders.set("X-Naver-Client-Id", XNaverClientId);
        httpHeaders.set("X-Naver-Client-Secret", XNaverClientSecret);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("source", sourceLanguage);
        body.add("target", targetLanguage);
        body.add("text", text);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity response = new RestTemplate().exchange(url, HttpMethod.POST, requestEntity, String.class);

        String jsonData = (String) response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonData);
            log.info(jsonNode.toString());
            JsonNode message=jsonNode.get("message");
            JsonNode result=message.get("result");
            translatedText = String.valueOf(result.get("translatedText")).replaceAll("[\"\\\\]", "");
            log.info(translatedText);

        } catch (Exception e) {

        }
        return translatedText;

    }
}
