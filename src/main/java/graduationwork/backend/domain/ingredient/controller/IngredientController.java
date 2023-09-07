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

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class IngredientController {
    private final UserRepository userRepository;
    private final IngredientService ingredientService;

    @PostMapping("/api/v1/ingredient-by-user")
    @Operation(summary = "재료 등록", description = "사용자님이 보유하고 있는 식재료들로 자신의 냉장고에 재료를 등록해주세요, 있는 식재료는 등록이 불가능합니다. 먼저 레시피로 만들어 드셔 보세요!")
    public ResponseEntity RegisterIngredient(@Valid @RequestBody IngredientRequestDto ingredientRequestDto, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            return ingredientService.registerIngredient(ingredientRequestDto, user.get());
        } else throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);

    }
}
