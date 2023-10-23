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

    @Autowired
    public VersionServiceImpl(CloudVisionTemplate cloudVisionTemplate, ResourceLoader resourceLoader) {
        this.cloudVisionTemplate = cloudVisionTemplate;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public List<Map<String, String>> detectLabelFromImage(MultipartFile file) {
        AnnotateImageResponse response = cloudVisionTemplate.analyzeImage(file.getResource(), Feature.Type.LABEL_DETECTION);
        List<EntityAnnotation> list = response.getLabelAnnotationsList();

        log.info(list.toString());

        List<Map<String, String>> responseList = new ArrayList<>();

        for (EntityAnnotation now : list) {
            Map<String, String> map = new HashMap<>();
            map.put("description", now.getDescription());
            map.put("score", String.valueOf(now.getScore()));
            responseList.add(map);
        }


        return responseList;
    }


}
