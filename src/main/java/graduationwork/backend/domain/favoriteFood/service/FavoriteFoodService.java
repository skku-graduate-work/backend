package graduationwork.backend.domain.favoriteFood.service;

import graduationwork.backend.domain.favoriteFood.domain.FavoriteFood;
import graduationwork.backend.domain.favoriteFood.dto.FavoriteFoodRequestDto;
import graduationwork.backend.domain.favoriteFood.dto.RandomFoodDto;
import graduationwork.backend.domain.favoriteFood.dto.RandomFoodListDto;
import graduationwork.backend.domain.favoriteFood.repository.FavoriteFoodRepository;
import graduationwork.backend.domain.food.domain.Food;
import graduationwork.backend.domain.food.repository.FoodRepository;
import graduationwork.backend.domain.user.domain.User;
import graduationwork.backend.global.error.exception.ConflictException;
import graduationwork.backend.global.error.exception.ErrorCode;
import graduationwork.backend.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FavoriteFoodService {
    private final FoodRepository foodRepository;
    private final FavoriteFoodRepository favoriteFoodRepository;
    @Transactional
    public ResponseEntity getRandomFood() {
        List<Food> foodList = foodRepository.getRandomFood();
        List<RandomFoodDto> randomFoodDtoList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            RandomFoodDto randomFoodDto = RandomFoodDto.builder()
                    .food_id(foodList.get(i).getId())
                    .name_en(foodList.get(i).getName_en())
                    .name_ko(foodList.get(i).getName_ko())
                    .image(foodList.get(i).getImage())
                    .build();
            randomFoodDtoList.add(randomFoodDto);
        }
        RandomFoodListDto randomFoodListDto=RandomFoodListDto.builder().randomFoodList(randomFoodDtoList).build();
        return ResponseEntity.status(HttpStatus.OK).body(randomFoodListDto);
    }

    @Transactional
    public ResponseEntity registerFavoriteFood(FavoriteFoodRequestDto favoriteFoodRequestDto, User user) {
        for (int i = 0; i < favoriteFoodRequestDto.getFavoriteFoodList().size(); i++) {
            log.info(String.valueOf(favoriteFoodRequestDto.getFavoriteFoodList().get(i)));
            Optional<Food> food = foodRepository.findById(Long.valueOf(favoriteFoodRequestDto.getFavoriteFoodList().get(i)));
            if (food.isPresent()) {
                FavoriteFood favoriteFood = FavoriteFood.builder()
                        .user(user)
                        .food_id(food.get().getId())
                        .name_ko(food.get().getName_ko())
                        .name_en(food.get().getName_en())
                        .image(food.get().getImage()).build();

                favoriteFoodRepository.save(favoriteFood);
            } else {
                throw new NotFoundException(ErrorCode.FOOD_NOT_FOUND);
            }

        }
        return ResponseEntity.status(HttpStatus.OK).body("저장되었습니다.");
    }

    @Transactional
    public ResponseEntity updateFavoriteFood(FavoriteFoodRequestDto favoriteFoodRequestDto, User user) {
        favoriteFoodRepository.deleteByUserId(user.getId());
        for (int i = 0; i < favoriteFoodRequestDto.getFavoriteFoodList().size(); i++) {
            Optional<Food> food = foodRepository.findById(Long.valueOf(favoriteFoodRequestDto.getFavoriteFoodList().get(i)));
            if (food.isPresent()) {
                FavoriteFood favoriteFood1=FavoriteFood.builder()
                        .user(user)
                        .food_id(food.get().getId())
                        .name_ko(food.get().getName_ko())
                        .name_en(food.get().getName_en())
                        .image(food.get().getImage()).build();
                favoriteFoodRepository.save(favoriteFood1);
            }else {
                throw new NotFoundException(ErrorCode.FOOD_NOT_FOUND);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("수정되었습니다.");
    }
}
