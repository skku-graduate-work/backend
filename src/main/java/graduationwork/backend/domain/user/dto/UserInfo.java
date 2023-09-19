package graduationwork.backend.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfo {
    private final String nickname;
    private final String profileImg;
    private final Float minCalories;
    private final Float minCarbs;
    private final Float minFat;
    private final Float minProtein;

    private final Float maxCalories;
    private final Float maxCarbs;
    private final Float maxFat;
    private final Float maxProtein;

    @Builder

    public UserInfo(String nickname, String profileImg, Float minCalories, Float minCarbs, Float minFat, Float minProtein, Float maxCalories, Float maxCarbs, Float maxFat, Float maxProtein) {
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.minCalories = minCalories;
        this.minCarbs = minCarbs;
        this.minFat = minFat;
        this.minProtein = minProtein;
        this.maxCalories = maxCalories;
        this.maxCarbs = maxCarbs;
        this.maxFat = maxFat;
        this.maxProtein = maxProtein;
    }
}
