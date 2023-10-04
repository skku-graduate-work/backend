package graduationwork.backend.domain.favoriteFood.controller;

import graduationwork.backend.domain.favoriteFood.dto.FavoriteFoodRequestDto;
import graduationwork.backend.domain.favoriteFood.service.FavoriteFoodService;
import graduationwork.backend.domain.user.domain.User;
import graduationwork.backend.domain.user.repository.UserRepository;
import graduationwork.backend.global.error.exception.ErrorCode;
import graduationwork.backend.global.error.exception.UnauthorizedException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorite-food")
public class FavoriteFoodController {

    private final FavoriteFoodService favoriteFoodService;
    private final UserRepository userRepository;
    @GetMapping("/random-food")
    @Operation(summary = "선호 음식 선택을 위한 음식 20개 가져 오기", description = "현재 음식 테이블에서 랜덤으로 20개의 음식을 가져옵니다.")
    public ResponseEntity getRandomFood() {
        return favoriteFoodService.getRandomFood();
    }

    @PostMapping()
    @Operation(summary = "선호 음식 등록하기", description = "랜덤으로 20개 요리 중에 사용자가 선호하는 음식을 선택해주세요.")
    public ResponseEntity registerFavoriteFood(@RequestBody FavoriteFoodRequestDto favoriteFoodRequestDto, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            return favoriteFoodService.registerFavoriteFood(favoriteFoodRequestDto, user.get());
        } else throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
    }

    @PatchMapping()
    @Operation(summary = "선호 음식 변경하기", description = "랜덤으로 20개 요리 중에 사용자가 선호하는 음식을 선택해주세요.")
    public ResponseEntity updateFavoriteFood(@RequestBody FavoriteFoodRequestDto favoriteFoodRequestDto, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            return favoriteFoodService.updateFavoriteFood(favoriteFoodRequestDto, user.get());
        } else throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
    }

}
