package graduationwork.backend.domain.ingredient.repository;

import graduationwork.backend.domain.food.domain.Food;
import graduationwork.backend.domain.ingredient.domain.Ingredient;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient,Long> {

    @Query("select i from Ingredient i where i.user.id=:userId and i.name=:name")
    Optional<Ingredient> findIngredientByUserIdAndName(@Param("userId") Long userId,@Param("name") String name);

    @Query("select i from Ingredient i where i.name=:name")
    Optional<Ingredient> findIngredientByName(@Param("name") String name);
}
