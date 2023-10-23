package graduationwork.backend.domain.FoodIngredient;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FoodResponseDto {
    private final String image;
    private final String name_ko;
    private final String name_en;

    @Builder
    public FoodResponseDto(String image, String name_ko, String name_en) {
        this.image = image;
        this.name_ko = name_ko;
        this.name_en = name_en;
    }
}
