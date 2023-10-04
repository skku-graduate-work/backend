package graduationwork.backend.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class FavoriteFoodInfo {
    private final String name_ko;
    private final String name_en;
    private final String image;
    private final Long food_id;

    @Builder
    public FavoriteFoodInfo(String name_ko, String name_en, String image,Long food_id) {
        this.name_ko = name_ko;
        this.name_en = name_en;
        this.image = image;
        this.food_id = food_id;
    }
}
