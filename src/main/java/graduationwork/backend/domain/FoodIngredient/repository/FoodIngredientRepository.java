package graduationwork.backend.domain.FoodIngredient.repository;

import graduationwork.backend.domain.FoodIngredient.domain.FoodIngredient;
import graduationwork.backend.domain.food.domain.Food;
import graduationwork.backend.domain.ingredient.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface FoodIngredientRepository extends JpaRepository<FoodIngredient, Long> {

//    @Query("select fi from FoodIngredient fi where fi.ingredient.name=:name")
//    List<Optional<FoodIngredient>> findFoodIngredientByIngredient(@Param("name") String name);

    @Query("SELECT f FROM Food f " +
            "JOIN FoodIngredient fi1 ON f.id = fi1.food.id AND fi1.ingredient.id = :ingredient1 " +
            "JOIN FoodIngredient fi2 ON f.id = fi2.food.id AND fi2.ingredient.id = :ingredient2 " +
            "JOIN FoodIngredient fi3 ON f.id = fi3.food.id AND fi3.ingredient.id = :ingredient3")
    List<Food> findFoodByIngredient(@Param("ingredient1") Long ingredient1, @Param("ingredient2") Long ingredient2, @Param("ingredient3") Long ingredient3);



}
