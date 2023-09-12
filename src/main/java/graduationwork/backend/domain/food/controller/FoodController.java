package graduationwork.backend.domain.food.controller;

import graduationwork.backend.domain.food.dto.IngredientRequestDtoForFood;
import graduationwork.backend.domain.food.service.FoodService;
import graduationwork.backend.domain.user.domain.User;
import graduationwork.backend.domain.user.repository.UserRepository;
import graduationwork.backend.global.error.exception.ErrorCode;
import graduationwork.backend.global.error.exception.UnauthorizedException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FoodController {
    private final FoodService foodService;
    private final UserRepository userRepository;

    @PostMapping("/api/v1/food-by-ingredient")
    @Operation(summary = "음식레시피 얻어오기", description = "냉장고 속에 있는 재료로 레시피를 얻어보세요!")
    public ResponseEntity GetIngredientForMakeFood(@RequestBody IngredientRequestDtoForFood ingredientRequestDtoForFood, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            return foodService.getIngredientForMakeFood(ingredientRequestDtoForFood, user.get());
        } else throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
    }
}
