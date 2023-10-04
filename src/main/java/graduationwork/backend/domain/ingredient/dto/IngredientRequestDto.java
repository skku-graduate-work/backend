package graduationwork.backend.domain.ingredient.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class IngredientRequestDto {
    @NotNull(message = "재료 이름이 입력되지 않았습니다")
    private String name;
    private  String expiration_date;
    private  Float calories;
    private Float carbs;
    private Float fat;
    private Float protein;

    @Builder
    public IngredientRequestDto(String name, String expiration_date, Float calories, Float carbs, Float fat, Float protein) {
        this.name = name;
        this.expiration_date = expiration_date;
        this.calories = calories;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
    }
}
