package graduationwork.backend.domain.favoriteFood.dto;

import graduationwork.backend.domain.food.domain.Food;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor // 데이터 유형이 기본 데이터 유형이므로 롬복이 자동으로 처리하기 어려워 기본생성자 추가
public class FavoriteFoodRequestDto {
    private List<Integer> favoriteFoodList;
    @Builder
    public FavoriteFoodRequestDto(List<Integer> favoriteFoodList) {
        this.favoriteFoodList = favoriteFoodList;
    }
}
