package graduationwork.backend.global;

import graduationwork.backend.domain.user.domain.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface VisionService {
    List<Map<String, String>> detectLabelFromImage(MultipartFile file);

}
