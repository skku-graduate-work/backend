package graduationwork.backend.domain.food.repository;

import graduationwork.backend.domain.food.domain.Food;
import graduationwork.backend.domain.ingredient.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food, Long> {

    @Query("select f from Food f where f.name=:name")
    Optional<Food> findFoodByName(@Param("name") String name);

    //TODO: 중복검사 쿼리 최적화 고민해보기
    @Query("select f from Food f join f.ingredientList fi join fi.ingredient i where i in :ingredients group by f having count(fi.ingredient)=:ingredientCount")
    List<Food> findFoodsByIngredients(@Param("ingredients") List<Ingredient> ingredientList, @Param("ingredientCount") int count);




}
