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
import org.springframework.web.bind.annotation.GetMapping;
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
    @Operation(summary = "음식레시피 얻어오기", description = "냉장고 속에 있는 3가지 재료로 5가지 음식 얻어오기")
    public ResponseEntity GetIngredientForMakeFood(@RequestBody IngredientRequestDtoForFood ingredientRequestDtoForFood, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            return foodService.getIngredientForMakeFood(ingredientRequestDtoForFood, user.get());
        } else throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
    }

    @GetMapping("/api/v1/food-by-nutrients")
    @Operation(summary = "음식레시피 얻어오기", description = "사용자가 설정한 영양정보로 5가지 레시피 얻어오기")
    public ResponseEntity GetFoodByNutrients(Authentication authentication) {
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            return foodService.getFoodByNutrients(user.get());
        } else throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
    }
}
