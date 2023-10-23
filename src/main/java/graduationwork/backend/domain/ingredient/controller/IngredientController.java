package graduationwork.backend.domain.ingredient.controller;

import com.google.rpc.context.AttributeContext;
import graduationwork.backend.domain.ingredient.service.IngredientService;
import graduationwork.backend.domain.ingredient.dto.IngredientRequestDto;

import graduationwork.backend.domain.user.domain.User;
import graduationwork.backend.domain.user.repository.UserRepository;
import graduationwork.backend.global.VisionService;
import graduationwork.backend.global.error.exception.ErrorCode;
import graduationwork.backend.global.error.exception.UnauthorizedException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class IngredientController {
    private final UserRepository userRepository;
    private final IngredientService ingredientService;
    private final VisionService visionService;

    @PostMapping(value = "/api/v1/ingredient-by-user", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "재료 등록", description = "사용자님이 보유하고 있는 식재료들로 자신의 냉장고에 재료를 등록해주세요, 있는 식재료는 등록이 불가능합니다.")
    public ResponseEntity RegisterIngredient(@Valid @RequestPart IngredientRequestDto ingredientRequestDto, @RequestPart(value = "image", required = false) MultipartFile file, Authentication authentication) throws IOException {
        log.info("사진: {}", file);
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return ingredientService.registerIngredient(ingredientRequestDto, file, user.get());
        } else throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);

    }

    @PostMapping(value = "/api/v1/ingredient/detect-label-from-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "사진 이미지로 해당 식재료 후보군 반환", description = "사용자님이 가지고 있는 재료 사진으로 이미지를 등록해주세요. 해당 이미지에 맞는 식재료 라벨을 추천해드릴게요")
    public List<Map<String, String>> DetectLabelFromImage(@RequestPart(value = "image") MultipartFile file, Authentication authentication) {
        return visionService.detectLabelFromImage(file);
    }


    @PostMapping(value = "/api/v1/ingredient/detect-label-from-image/user-result")
    @Operation(summary = "사용자님이 선택한 식재료를 보내주세요", description = "사용자님이 선택한 식재료를 보내주세요, 냉장고에 등록해드릴게요")
    public ResponseEntity RegisterIngredientByUserSelection(String name_ko, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return ingredientService.registerIngredientByUserSelection(name_ko, user.get());
        } else throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
    }
}
