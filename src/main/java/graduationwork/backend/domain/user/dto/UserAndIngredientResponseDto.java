package graduationwork.backend.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class UserAndIngredientResponseDto {
    private final UserInfo user;
    private final List<IngredientInfo> ingredients;
    @Builder
    public UserAndIngredientResponseDto(UserInfo user, List<IngredientInfo> ingredients) {
        this.user = user;
        this.ingredients = ingredients;
    }
}
