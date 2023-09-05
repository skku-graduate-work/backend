package graduationwork.backend.domain.ingredient.domain;

import graduationwork.backend.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private String name;
    private String image;
    private LocalDate expiration_date;
    private Float calories;
    private Float carbs;
    private Float fat;
    private Float protein;

    @Builder
    public Ingredient(User user, String name, String image, LocalDate expiration_date, Float calories, Float carbs, Float fat, Float protein) {
        this.user = user;
        this.name = name;
        this.image = image;
        this.expiration_date = expiration_date;
        this.calories = calories;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
    }
}
