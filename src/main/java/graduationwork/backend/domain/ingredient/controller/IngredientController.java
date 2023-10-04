package graduationwork.backend.domain.ingredient.controller;

import graduationwork.backend.domain.ingredient.service.IngredientService;
import graduationwork.backend.domain.ingredient.dto.IngredientRequestDto;

import graduationwork.backend.domain.user.domain.User;
import graduationwork.backend.domain.user.repository.UserRepository;
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
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class IngredientController {
    private final UserRepository userRepository;
    private final IngredientService ingredientService;

    @PostMapping(value="/api/v1/ingredient-by-user",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "재료 등록", description = "사용자님이 보유하고 있는 식재료들로 자신의 냉장고에 재료를 등록해주세요, 있는 식재료는 등록이 불가능합니다.")
    public ResponseEntity RegisterIngredient(@Valid @RequestPart IngredientRequestDto ingredientRequestDto, @RequestPart(value="image",required = false) MultipartFile file, Authentication authentication) throws IOException {
        log.info("사진: {}", file);
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return ingredientService.registerIngredient(ingredientRequestDto,file, user.get());
        } else throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);

    }
}
