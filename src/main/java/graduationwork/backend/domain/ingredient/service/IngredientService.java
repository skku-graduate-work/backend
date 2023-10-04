package graduationwork.backend.domain.ingredient.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import graduationwork.backend.domain.ingredient.domain.Ingredient;
import graduationwork.backend.domain.ingredient.dto.IngredientRequestDto;
import graduationwork.backend.domain.ingredient.repository.IngredientRepository;
import graduationwork.backend.domain.user.domain.User;
import graduationwork.backend.global.S3Service;
import graduationwork.backend.global.TranslateService;
import graduationwork.backend.global.error.exception.ConflictException;
import graduationwork.backend.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final TranslateService translateService;
    private final S3Service s3Service;



    @Transactional
    public ResponseEntity registerIngredient(IngredientRequestDto ingredientRequestDto, MultipartFile file, User user) throws IOException {
        Optional<Ingredient> ingredient = ingredientRepository.findIngredientByUserIdAndNameKo(user.getId(), ingredientRequestDto.getName());
        String name_en = translateService.translateText("ko", "en", ingredientRequestDto.getName());
        log.info(name_en);
        String imgPath = "";
        if (file!=null) imgPath = s3Service.upload(file);
        if (ingredient.isEmpty()) {
            ingredientRepository.save(Ingredient.builder()
                    .user(user)
                    .name_ko(ingredientRequestDto.getName())
                    .name_en(name_en)
                    .fat(ingredientRequestDto.getFat())
                    .image(imgPath)
                    .carbs(ingredientRequestDto.getCarbs())
                    .calories(ingredientRequestDto.getCalories())
                    .protein(ingredientRequestDto.getProtein())
                    .expiration_date(LocalDate.parse(ingredientRequestDto.getExpiration_date()))
                    .build());
            return ResponseEntity.status(HttpStatus.OK).body("성공적으로 등록되었습니다");
        } else {
            throw new ConflictException(ErrorCode.CONFLICT_INGREDIENT);
        }

    }


}
