package graduationwork.backend.global;

import com.google.cloud.spring.vision.CloudVisionTemplate;
import com.google.cloud.vision.v1.AnnotateFileResponse;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.nimbusds.oauth2.sdk.Response;
import graduationwork.backend.domain.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class VersionServiceImpl implements VisionService {

    private final CloudVisionTemplate cloudVisionTemplate;
    private final ResourceLoader resourceLoader;
    private final TranslateService translateService;


    @Autowired
    public VersionServiceImpl(CloudVisionTemplate cloudVisionTemplate, ResourceLoader resourceLoader, TranslateService translateService) {
        this.cloudVisionTemplate = cloudVisionTemplate;
        this.resourceLoader = resourceLoader;
        this.translateService = translateService;
    }

    @Override
    public List<Map<String, String>> detectLabelFromImage(MultipartFile file) {
        AnnotateImageResponse response = cloudVisionTemplate.analyzeImage(file.getResource(), Feature.Type.LABEL_DETECTION);
        List<EntityAnnotation> list = response.getLabelAnnotationsList();

        List<Map<String, String>> responseList = new ArrayList<>();

        for (EntityAnnotation now : list) {
            Map<String, String> map = new HashMap<>();
            map.put("description_en", now.getDescription());
            String description_ko=translateService.translateText("en", "ko", now.getDescription());
            map.put("description_ko", description_ko);
            map.put("score", String.valueOf(now.getScore()));
            responseList.add(map);
        }

        return responseList;
    }


}
