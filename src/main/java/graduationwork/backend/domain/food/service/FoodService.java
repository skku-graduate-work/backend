package graduationwork.backend.domain.food.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import graduationwork.backend.domain.FoodIngredient.domain.FoodIngredient;
import graduationwork.backend.domain.FoodIngredient.repository.FoodIngredientRepository;
import graduationwork.backend.domain.food.domain.Food;
import graduationwork.backend.domain.food.dto.IngredientRequestDtoForFood;
import graduationwork.backend.domain.food.repository.FoodRepository;
import graduationwork.backend.domain.ingredient.domain.Ingredient;
import graduationwork.backend.domain.ingredient.repository.IngredientRepository;
import graduationwork.backend.domain.user.domain.User;
import graduationwork.backend.global.config.ApiConfig;
import graduationwork.backend.global.error.exception.BadRequestException;
import graduationwork.backend.global.error.exception.ConflictException;
import graduationwork.backend.global.error.exception.ErrorCode;
import graduationwork.backend.global.error.exception.NotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpClient;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FoodService {

    private final ApiConfig apiConfig;
    private final FoodRepository foodRepository;
    private final IngredientRepository ingredientRepository;
    private final FoodIngredientRepository foodIngredientRepository;

    @Transactional
    public ResponseEntity getIngredientForMakeFood(IngredientRequestDtoForFood ingredientRequestDtoForFood, User user) {
        Optional<Ingredient> ingredient1= ingredientRepository.findIngredientByName(ingredientRequestDtoForFood.getFood1());
        Optional<Ingredient> ingredient2= ingredientRepository.findIngredientByName(ingredientRequestDtoForFood.getFood2());
        Optional<Ingredient> ingredient3= ingredientRepository.findIngredientByName(ingredientRequestDtoForFood.getFood3());

        if (ingredient1.isEmpty() || ingredient2.isEmpty() ||ingredient3.isEmpty() ) {
            throw new NotFoundException(ErrorCode.INGREDIENT_NOT_FOUND);
        }
        List<Ingredient> selectedIngredients = new ArrayList<>();
        selectedIngredients.add(ingredient1.get());
        selectedIngredients.add(ingredient2.get());
        selectedIngredients.add(ingredient3.get());

        if (!foodRepository.findFoodsByIngredients(selectedIngredients,3).isEmpty()) {
            throw new ConflictException(ErrorCode.CONFLICT_FOOD);
        }
        // TODO: 중복 검사
//        if (foodIngredientRepository.findFoodIngredientByIngredient(ingredientRequestDtoForFood.getFood1()).isPresent() && foodIngredientRepository.findFoodIngredientByIngredient(ingredient2.get().getName()).isPresent() && foodIngredientRepository.findFoodIngredientByIngredient(ingredient3.get().getName()).isPresent()  ) {
//            log.info(ErrorCode.CONFLICT_FOOD.getMessage());
//            throw new ConflictException(ErrorCode.CONFLICT_FOOD);
//        }
        ResponseEntity response = getResponse(ingredientRequestDtoForFood);
        return ResponseEntity.ok(saveFoodByIngredient(response, ingredient1.get(),ingredient2.get(),ingredient3.get(),user));
    }
    /**
     * API 호출
     * @param ingredientRequestDtoForFood
     * @return 3가지 재료로 만들 수 있는 음식 반환 하기 +
     */
    private ResponseEntity getResponse(IngredientRequestDtoForFood ingredientRequestDtoForFood) {

        final String url = "https://api.spoonacular.com/recipes/findByIngredients";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("x-api-key", apiConfig.getKey());


        Map<String, Object> params = new HashMap<>();
        params.put("ingredients", ingredientRequestDtoForFood.getFood1() + ",+" + ingredientRequestDtoForFood.getFood2() + ",+" + ingredientRequestDtoForFood.getFood3());
        params.put("number", 5);

        HttpEntity<?> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity response = new RestTemplate().exchange(
                url + "?ingredients={ingredients}&number={number}",
                HttpMethod.GET,
                requestEntity,
                String.class,
                params
        );
        return response;
    }

    /**
     *
     * @param response
     * @param ingredient1
     * @param ingredient2
     * @param ingredient3
     * @param user
     * @return 사용자가 선택한 냉장고 속 음식에 따라 레시피 5개 반환
     */
    private List<Map<String,String>> saveFoodByIngredient(ResponseEntity response,Ingredient ingredient1,Ingredient ingredient2,Ingredient ingredient3,User user) {
        String jsonData = (String) response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, String>> responseList = new ArrayList<>();

        try {
            JsonNode foodArray = objectMapper.readTree(jsonData);
            log.info(String.valueOf(foodArray.size()));
            for (JsonNode foodNode : foodArray) {
                String name = foodNode.get("title").toString();
                String image = foodNode.get("image").toString();
                log.info("음식: {}, 이미지: {}", name, image);
                // 음식 저장
                Food food = foodRepository.save(Food.builder().name(name).image(image).user(user).build());
                foodIngredientRepository.save(FoodIngredient.builder().ingredient(ingredient1).food(food).build());
                foodIngredientRepository.save(FoodIngredient.builder().ingredient(ingredient2).food(food).build());
                foodIngredientRepository.save(FoodIngredient.builder().ingredient(ingredient3).food(food).build());

                Map<String, String> foodInfo = new HashMap<>();
                foodInfo.put("name", name);
                foodInfo.put("image", image);
                responseList.add(foodInfo);



            }
        } catch (Exception e) {

        }
        return responseList;
    }


}
