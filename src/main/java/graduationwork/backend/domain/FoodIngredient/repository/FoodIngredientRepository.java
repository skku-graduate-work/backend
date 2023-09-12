package graduationwork.backend.domain.FoodIngredient.repository;

import graduationwork.backend.domain.FoodIngredient.domain.FoodIngredient;
import graduationwork.backend.domain.ingredient.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FoodIngredientRepository extends JpaRepository<FoodIngredient, Long> {

//    @Query("select fi from FoodIngredient fi where fi.ingredient.name=:name")
//    List<Optional<FoodIngredient>> findFoodIngredientByIngredient(@Param("name") String name);

}
