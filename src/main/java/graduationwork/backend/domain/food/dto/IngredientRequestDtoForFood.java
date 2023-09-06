package graduationwork.backend.domain.food.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class IngredientRequestDtoForFood {
    private final String food1;
    private final String food2;
    private final String food3;

    @Builder
    public IngredientRequestDtoForFood(String food1, String food2, String food3) {
        this.food1 = food1;
        this.food2 = food2;
        this.food3 = food3;
    }
}
