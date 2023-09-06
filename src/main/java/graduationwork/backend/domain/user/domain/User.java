package graduationwork.backend.domain.user.domain;

import graduationwork.backend.domain.food.domain.Food;
import graduationwork.backend.domain.ingredient.domain.Ingredient;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String nickname;
    private String password;


    private Float minCalories;
    private Float minCarbs;
    private Float minFat;
    private Float minProtein;

    private Float maxCalories;
    private Float maxCarbs;
    private Float maxFat;
    private Float maxProtein;

    private String profileImg;
    private String refreshToken;

    @OneToMany(mappedBy = "user")
    private List<Ingredient> ingredientList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Food> foodList = new ArrayList<>();


    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }




    @Builder
    public User(String email, String nickname, String password, Float minCalories, Float minCarbs, Float minFat, Float minProtein, Float maxCalories, Float maxCarbs, Float maxFat, Float maxProtein, String profileImg, String refreshToken) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.minCalories = minCalories;
        this.minCarbs = minCarbs;
        this.minFat = minFat;
        this.minProtein = minProtein;
        this.maxCalories = maxCalories;
        this.maxCarbs = maxCarbs;
        this.maxFat = maxFat;
        this.maxProtein = maxProtein;
        this.profileImg = profileImg;
        this.refreshToken = refreshToken;
    }

    public void updateMinNutrients(Float minCalories, Float minCarbs, Float minFat, Float minProtein) {
        this.minCalories = minCalories;
        this.minCarbs = minCarbs;
        this.minFat = minFat;
        this.minProtein = minProtein;

    }
    public void updateMaxNutrients(Float maxCalories, Float maxCarbs, Float maxFat, Float maxProtein) {
        this.maxCalories = maxCalories;
        this.maxCarbs = maxCarbs;
        this.maxFat = maxFat;
        this.maxProtein = maxProtein;

    }
}
