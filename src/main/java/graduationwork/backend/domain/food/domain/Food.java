package graduationwork.backend.domain.food.domain;

import graduationwork.backend.domain.FoodIngredient.domain.FoodIngredient;
import graduationwork.backend.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    private String name_ko;
    private String name_en;
    private String image;

    @OneToMany(mappedBy = "food")
    private List<FoodIngredient> ingredientList = new ArrayList<>();


    @Builder
    public Food(User user, String name_ko,String name_en, String image) {
        this.user = user;
        this.name_ko = name_ko;
        this.name_en = name_en;
        this.image = image;
    }
}
