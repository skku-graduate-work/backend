package graduationwork.backend.domain.favoriteFood.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class RandomFoodDto {
    private final Long food_id;
    private final String name_ko;
    private final String name_en;
    private final String image;
    private final Long spoonacular_food_id;

    @Builder
    public RandomFoodDto(Long food_id,String name_ko, String name_en, String image, Long spoonacular_food_id) {
        this.food_id = food_id;
        this.name_ko = name_ko;
        this.name_en = name_en;
        this.image = image;
        this.spoonacular_food_id = spoonacular_food_id;
    }
}
