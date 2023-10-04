package graduationwork.backend.domain.favoriteFood.repository;

import graduationwork.backend.domain.favoriteFood.domain.FavoriteFood;
import graduationwork.backend.domain.food.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FavoriteFoodRepository extends JpaRepository<FavoriteFood, Long> {

    @Query("select f from FavoriteFood f where f.food_id=:id")
    Optional<FavoriteFood> findByFoodId(@Param("id")Long id);

    void deleteByUserId(Long id);
}
