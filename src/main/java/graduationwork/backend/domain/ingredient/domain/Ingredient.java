package graduationwork.backend.domain.ingredient.domain;

import graduationwork.backend.domain.FoodIngredient.domain.FoodIngredient;
import graduationwork.backend.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    private String name_ko;
    private String name_en;

    private String image;
    private LocalDate expiration_date;
    private Float calories;
    private Float carbs;
    private Float fat;
    private Float protein;

    @OneToMany(mappedBy = "ingredient")
    private List<FoodIngredient> Foodlist = new ArrayList<>();

    @Builder
    public Ingredient(User user, String name_ko, String name_en,String image, LocalDate expiration_date, Float calories, Float carbs, Float fat, Float protein) {
        this.user = user;
        this.name_ko = name_ko;
        this.name_en = name_en;
        this.image = image;
        this.expiration_date = expiration_date;
        this.calories = calories;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
    }
}
