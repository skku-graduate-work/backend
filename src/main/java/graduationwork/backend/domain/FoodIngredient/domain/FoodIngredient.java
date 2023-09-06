package graduationwork.backend.domain.FoodIngredient.domain;

import graduationwork.backend.domain.food.domain.Food;
import graduationwork.backend.domain.ingredient.domain.Ingredient;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FoodIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @Builder
    public FoodIngredient(Food food, Ingredient ingredient) {
        this.food = food;
        this.ingredient = ingredient;
    }
}
