package graduationwork.backend.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NutrientsRequestDto {
    private final Float calories;
    private final Float carbs;
    private final Float fat;
    private final Float protein;

    @Builder
    public NutrientsRequestDto(Float calories, Float carbs, Float fat, Float protein) {
        this.calories = calories;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
    }
}
