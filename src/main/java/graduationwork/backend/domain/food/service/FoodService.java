package graduationwork.backend.domain.food.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import graduationwork.backend.domain.FoodIngredient.FoodResponseDto;
import graduationwork.backend.domain.FoodIngredient.domain.FoodIngredient;
import graduationwork.backend.domain.FoodIngredient.repository.FoodIngredientRepository;
import graduationwork.backend.domain.food.domain.Food;
import graduationwork.backend.domain.food.dto.IngredientRequestDtoForFood;
import graduationwork.backend.domain.food.repository.FoodRepository;
import graduationwork.backend.domain.ingredient.domain.Ingredient;
import graduationwork.backend.domain.ingredient.repository.IngredientRepository;
import graduationwork.backend.domain.user.domain.User;
import graduationwork.backend.global.TranslateService;
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
    private final TranslateService translateService;
    @Transactional
    public ResponseEntity getIngredientForMakeFood(IngredientRequestDtoForFood ingredientRequestDtoForFood, User user) {
        Optional<Ingredient> ingredient1= ingredientRepository.findIngredientByNameKo(ingredientRequestDtoForFood.getFood1(),user.getId());
        Optional<Ingredient> ingredient2= ingredientRepository.findIngredientByNameKo(ingredientRequestDtoForFood.getFood2(),user.getId());
        Optional<Ingredient> ingredient3= ingredientRepository.findIngredientByNameKo(ingredientRequestDtoForFood.getFood3(),user.getId());

        if (ingredient1.isEmpty() || ingredient2.isEmpty() ||ingredient3.isEmpty() ) {
            log.info(ErrorCode.INGREDIENT_NOT_FOUND.getMessage());
            throw new NotFoundException(ErrorCode.INGREDIENT_NOT_FOUND);
        }
        List<Ingredient> selectedIngredients = new ArrayList<>();
        selectedIngredients.add(ingredient1.get());
        selectedIngredients.add(ingredient2.get());
        selectedIngredients.add(ingredient3.get());
        log.info(ingredient1.get().getName_en());

        if (!foodRepository.findFoodsByIngredients(selectedIngredients,3).isEmpty()) {
            log.info(ErrorCode.CONFLICT_FOOD.getMessage());
            log.info(String.valueOf(ingredient1.get().getId()));
            log.info(String.valueOf(ingredient2.get().getId()));
            log.info(String.valueOf(ingredient3.get().getId()));
            List<Food> foodResponse= foodIngredientRepository.findFoodByIngredient(ingredient1.get().getId(), ingredient2.get().getId(), ingredient3.get().getId());
            log.info(String.valueOf(foodResponse.size()));
            List<FoodResponseDto> foodResponseDtoList = getFoodResponse(foodResponse);
            return ResponseEntity.ok(foodResponseDtoList);
        }

        ResponseEntity response = getResponse(ingredientRequestDtoForFood, user);
        return ResponseEntity.ok(saveFoodByIngredient(response, ingredient1.get(),ingredient2.get(),ingredient3.get(),user));
    }

    private List<FoodResponseDto> getFoodResponse(List<Food> foodResponse) {
        List<FoodResponseDto> result = new ArrayList<>();
        for (int i = 0; i < foodResponse.size(); i++) {
            FoodResponseDto foodResponseDto = FoodResponseDto.builder()
                    .spoonacular_food_id(foodResponse.get(i).getSpoonacular_food_id())
                    .image(foodResponse.get(i).getImage())
                    .name_ko(foodResponse.get(i).getName_ko())
                    .name_en(foodResponse.get(i).getName_en())
                    .build();

            result.add(foodResponseDto);
        }
        return result;
    }

    /**
     * API 호출
     * @param ingredientRequestDtoForFood
     * @return 3가지 재료로 만들 수 있는 음식 반환 하기 +
     */
    private ResponseEntity getResponse(IngredientRequestDtoForFood ingredientRequestDtoForFood,User user) {

        final String url = "https://api.spoonacular.com/recipes/findByIngredients";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("x-api-key", apiConfig.getKey());
        Ingredient ingredient1 = ingredientRepository.findIngredientByNameKo(ingredientRequestDtoForFood.getFood1(),user.getId()).get();
        Ingredient ingredient2 = ingredientRepository.findIngredientByNameKo(ingredientRequestDtoForFood.getFood2(),user.getId()).get();
        Ingredient ingredient3 = ingredientRepository.findIngredientByNameKo(ingredientRequestDtoForFood.getFood3(),user.getId()).get();


        Map<String, Object> params = new HashMap<>();
        params.put("ingredients", ingredient1.getName_en() + ",+" + ingredient2.getName_en() + ",+" + ingredient3.getName_en());
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
        log.info(jsonData);
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, String>> responseList = new ArrayList<>();

        try {
            JsonNode foodArray = objectMapper.readTree(jsonData);
            log.info(String.valueOf(foodArray.size()));
            for (JsonNode foodNode : foodArray) {
                String name_en = foodNode.get("title").toString();
                String image = foodNode.get("image").toString();
                String spoonacular_food_id = foodNode.get("id").toString();
                log.info("음식: {}, 이미지: {}", name_en, image);
                Map<String, String> foodInfo = new HashMap<>();
                foodInfo.put("name_en", name_en);
                String name_ko=translateService.translateText("en", "ko", name_en);
                foodInfo.put("name_ko", name_ko);
                foodInfo.put("image", image);
                foodInfo.put("spoonacular_food_id", spoonacular_food_id);
                responseList.add(foodInfo);

                // 음식 저장
                Food food = foodRepository.save(Food.builder().name_en(name_en.replaceAll("\"", "")).name_ko(name_ko).image(image).user(user).spoonacular_food_id(Long.valueOf(spoonacular_food_id)).build());
                foodIngredientRepository.save(FoodIngredient.builder().ingredient(ingredient1).food(food).build());
                foodIngredientRepository.save(FoodIngredient.builder().ingredient(ingredient2).food(food).build());
                foodIngredientRepository.save(FoodIngredient.builder().ingredient(ingredient3).food(food).build());

            }
        } catch (Exception e) {

        }
        return responseList;
    }


    @Transactional
    public ResponseEntity getFoodByNutrients(User user) {
        float min_calories = user.getMinCalories();
        float min_carbs = user.getMinCarbs();
        float min_fat = user.getMinFat();
        float min_protein = user.getMinProtein();

        float max_calories = user.getMaxCalories();
        float max_carbs = user.getMaxCarbs();
        float max_fat = user.getMaxFat();
        float max_protein = user.getMaxProtein();

        List<Map<String, String>> response = getMinResponse(min_calories, min_carbs, min_fat, min_protein, user);

        return ResponseEntity.ok(response);
    }

    private  List<Map<String, String>> getMinResponse(float minCalories, float minCarbs, float minFat, float minProtein, User user) {
        final String url = "https://api.spoonacular.com/recipes/findByNutrients";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("x-api-key", apiConfig.getKey());


        Map<String, Object> params = new HashMap<>();
        params.put("minCarbs", minCarbs);
        params.put("minCalories", minCalories);
        params.put("minFat", minFat);
        params.put("minProtein", minProtein);

        params.put("number", 5);

        HttpEntity<?> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity response = new RestTemplate().exchange(
                url + "?minCarbs={minCarbs}&minCalories={minCalories}&minFat={minFat}&minProtein={minProtein}&number={number}",
                HttpMethod.GET,
                requestEntity,
                String.class,
                params
        );
        List<Map<String, String>> result= FoodByNutrients(response);
        return result;
    }

    private  List<Map<String, String>> FoodByNutrients(ResponseEntity response) {
        String jsonData = (String) response.getBody();
        log.info(jsonData);
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, String>> responseList = new ArrayList<>();

        try {
            JsonNode foodArray = objectMapper.readTree(jsonData);
            log.info(String.valueOf(foodArray.size()));
            for (JsonNode foodNode : foodArray) {
                String name_en = foodNode.get("title").toString();
                String image = foodNode.get("image").toString();
                log.info("음식: {}, 이미지: {}", name_en, image);
                Map<String, String> foodInfo = new HashMap<>();
                foodInfo.put("name_en", name_en);
                String name_ko=translateService.translateText("en", "ko", name_en);
                foodInfo.put("name_ko", name_ko);
                foodInfo.put("image", image);
                responseList.add(foodInfo);
            }
        } catch (Exception e) {

        }
        return responseList;

    }
}
