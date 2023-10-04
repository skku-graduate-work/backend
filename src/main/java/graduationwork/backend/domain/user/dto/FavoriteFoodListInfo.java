package graduationwork.backend.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class FavoriteFoodListInfo {
    private final List<FavoriteFoodInfo> foodInfoList;

    @Builder
    public FavoriteFoodListInfo(List<FavoriteFoodInfo> foodInfoList) {
        this.foodInfoList = foodInfoList;
    }
}
