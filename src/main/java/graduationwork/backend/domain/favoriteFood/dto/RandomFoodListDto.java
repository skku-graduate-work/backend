package graduationwork.backend.domain.favoriteFood.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class RandomFoodListDto {
    private final List<RandomFoodDto> randomFoodList;

    @Builder
    public RandomFoodListDto(List<RandomFoodDto> randomFoodList) {
        this.randomFoodList = randomFoodList;
    }
}
