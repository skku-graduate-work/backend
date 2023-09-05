package graduationwork.backend.domain.ingredient.service;

import graduationwork.backend.domain.ingredient.domain.Ingredient;
import graduationwork.backend.domain.ingredient.dto.IngredientRequestDto;
import graduationwork.backend.domain.ingredient.repository.IngredientRepository;
import graduationwork.backend.domain.user.domain.User;
import graduationwork.backend.global.error.exception.ConflictException;
import graduationwork.backend.global.error.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    @Transactional
    public ResponseEntity registerIngredient( IngredientRequestDto ingredientRequestDto, User user) {
        Optional<Ingredient> ingredient = ingredientRepository.findIngredientByUserIdAndName(user.getId(), ingredientRequestDto.getName());

        if (ingredient.isEmpty()) {
            ingredientRepository.save(Ingredient.builder()
                    .user(user)
                    .name(ingredientRequestDto.getName())
                    .image(ingredientRequestDto.getImage())
                    .fat(ingredientRequestDto.getFat())
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
