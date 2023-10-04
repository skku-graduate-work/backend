package graduationwork.backend.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class UserAndIngredientAndFavoriteFoodResponseDto {
    private final UserInfo user;
    private final List<IngredientInfo> ingredients;
    private final List<FavoriteFoodInfo> favoriteFoodInfoList;

    @Builder
    public UserAndIngredientAndFavoriteFoodResponseDto(UserInfo user, List<IngredientInfo> ingredients, List<FavoriteFoodInfo> foodInfoList) {
        this.user = user;
        this.ingredients = ingredients;
        this.favoriteFoodInfoList = foodInfoList;
    }
}
