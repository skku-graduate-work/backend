package graduationwork.backend.domain.user.service;

import graduationwork.backend.domain.favoriteFood.domain.FavoriteFood;
import graduationwork.backend.domain.ingredient.domain.Ingredient;
import graduationwork.backend.domain.ingredient.repository.IngredientRepository;
import graduationwork.backend.domain.user.domain.User;
import graduationwork.backend.domain.user.dto.*;
import graduationwork.backend.domain.user.repository.UserRepository;
import graduationwork.backend.global.S3Service;
import graduationwork.backend.global.error.exception.BadRequestException;
import graduationwork.backend.global.error.exception.ConflictException;
import graduationwork.backend.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IngredientRepository ingredientRepository;
    private final S3Service s3Service;


    @Transactional
    public ResponseEntity join(SignUpRequestDto signUpRequestDto) {
        String email = signUpRequestDto.getEmail();
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new ConflictException(ErrorCode.CONFLICT);
        } else {
            User user = User.builder().email(signUpRequestDto.getEmail()).password(signUpRequestDto.getPassword()).build();
            user.passwordEncode(passwordEncoder);
            userRepository.save(user);
            log.info(user.getEmail() + " 회원가입 성공");
            return ResponseEntity.status(HttpStatus.CREATED).build();


        }

    }

    @Transactional
    public ResponseEntity setMinNutrients(NutrientsRequestDto nutrientsRequestDto, User user) {
        user.updateMinNutrients(nutrientsRequestDto.getCalories(), nutrientsRequestDto.getCarbs(), nutrientsRequestDto.getFat() , nutrientsRequestDto.getProtein());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("성공적으로 등록되었습니다");
    }

    @Transactional
    public ResponseEntity setMaxNutrients(NutrientsRequestDto nutrientsRequestDto, User user) {
        log.info(nutrientsRequestDto.getCalories().toString());
        user.updateMaxNutrients(nutrientsRequestDto.getCalories(), nutrientsRequestDto.getCarbs(), nutrientsRequestDto.getFat() , nutrientsRequestDto.getProtein());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("성공적으로 등록되었습니다");
    }

    @Transactional
    public ResponseEntity getUserAndIngredientAndFavoriteFood(User user) {
        Optional<User> findUser = userRepository.findByEmail(user.getEmail());
        UserAndIngredientAndFavoriteFoodResponseDto userAndIngredientResponseDto=null;
        if (findUser.isPresent()) {
            UserInfo userInfo = getUserInfo(findUser.get());
            List<IngredientInfo> ingredientInfoList = getIngredientInfoList(findUser.get());
            List<FavoriteFoodInfo> foodInfoList = getFavoriteFoodInfoList(findUser.get());
            userAndIngredientResponseDto = UserAndIngredientAndFavoriteFoodResponseDto.builder()
                    .foodInfoList(foodInfoList)
                    .ingredients(ingredientInfoList)
                    .user(userInfo)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(userAndIngredientResponseDto);

        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorCode.ENTITY_NOT_FOUND);

    }

    private List<FavoriteFoodInfo> getFavoriteFoodInfoList(User findUser) {
        List<FavoriteFood> favoriteFoodList = findUser.getFavoriteFoodList();
        List<FavoriteFoodInfo> favoriteFoodInfoList = new ArrayList<>();
        if (favoriteFoodList.isEmpty()) {
            return null;
        } else {
            for (FavoriteFood food: favoriteFoodList) {
                FavoriteFoodInfo favoriteFoodInfo = FavoriteFoodInfo.builder()
                        .food_id(food.getFood_id())
                        .name_ko(food.getName_ko())
                        .name_en(food.getName_en())
                        .image(food.getImage())
                        .build();
                favoriteFoodInfoList.add(favoriteFoodInfo);
            }
            return favoriteFoodInfoList;
        }
    }

    private List<IngredientInfo> getIngredientInfoList(User findUser) {
        List<Ingredient> ingredientList=ingredientRepository.findIngredientsByUser(findUser.getId());
        List<IngredientInfo> ingredientInfoList = new ArrayList<>();
        if (ingredientList.isEmpty()) {
            return null;
        } else {
            for (Ingredient now : ingredientList) {
                IngredientInfo ingredientInfo = IngredientInfo.builder()
                        .name_en(now.getName_en())
                        .name_ko(now.getName_ko())
                        .image(now.getImage())
                        .fat(now.getFat())
                        .calories(now.getCalories())
                        .protein(now.getProtein())
                        .carbs(now.getCarbs())
                        .build();
                ingredientInfoList.add(ingredientInfo);
            }
            return ingredientInfoList;
        }

    }

    private static UserInfo getUserInfo(User findUser) {
        return UserInfo.builder()
                .nickname(findUser.getNickname())
                .email(findUser.getEmail())
                .profileImg(findUser.getProfileImg())
                .minCalories(findUser.getMinCalories())
                .minCarbs(findUser.getMinCarbs())
                .minFat(findUser.getMinFat())
                .minProtein(findUser.getMinProtein())
                .maxProtein(findUser.getMaxProtein())
                .maxFat(findUser.getMaxFat())
                .maxCarbs(findUser.getMaxCarbs())
                .maxCalories(findUser.getMaxCalories())
                .build();
    }

    @Transactional
    public ResponseEntity updateProfileImage(MultipartFile file, User user) {
        try {
            String imgPath = s3Service.upload(file);
            Optional<User> findUser = userRepository.findByEmail(user.getEmail());
            if (findUser.isPresent()) {
                log.info(imgPath);
                findUser.get().updateProfileImage(imgPath);
                return ResponseEntity.status(HttpStatus.OK).body("성공적으로 프로필 사진이 수정되었습니다");
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorCode.ENTITY_NOT_FOUND);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new BadRequestException();
        }



    }
}

