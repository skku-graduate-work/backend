package graduationwork.backend.domain.favoriteFood.domain;

import graduationwork.backend.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteFood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    private String name_ko;
    private String name_en;
    private String image;
    private Long food_id;

    @Builder
    public FavoriteFood(User user, String name_ko, String name_en, String image, Long food_id) {
        this.user = user;
        this.name_ko = name_ko;
        this.name_en = name_en;
        this.image = image;
        this.food_id = food_id;
    }
}
