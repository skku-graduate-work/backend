package graduationwork.backend.domain.user.controller;

import graduationwork.backend.domain.user.domain.User;
import graduationwork.backend.domain.user.repository.UserRepository;
import graduationwork.backend.domain.user.dto.NutrientsRequestDto;
import graduationwork.backend.domain.user.dto.SignUpRequestDto;
import graduationwork.backend.global.S3Service;
import graduationwork.backend.global.error.exception.ErrorCode;
import graduationwork.backend.global.error.exception.UnauthorizedException;
import graduationwork.backend.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    public ResponseEntity SignUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        return userService.join(signUpRequestDto);
    }

    @PostMapping(value="/update-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "프로필 이미지 변경", description = "s3 와 연동되어 프로필 이미지를 변경 가능")
    public ResponseEntity updateProfileImage(@RequestParam(value="image") MultipartFile file,Authentication authentication) throws IOException {
        log.info("이미지 변경");
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            return userService.updateProfileImage(file, user.get());
        } else throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);


    }
    @PutMapping("/min-nutrients")
    @Operation(summary = "개인 영양소 하한선 설정", description = "칼로리, 탄수화물, 지방, 단백질 하한선 설정하기")
    public ResponseEntity SetMinNutrients(@RequestBody NutrientsRequestDto nutrientsRequestDto, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            return userService.setMinNutrients(nutrientsRequestDto, user.get());
        } else throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);

    }

    @PutMapping("/max-nutrients")
    @Operation(summary = "개인 영양소 상한선 설정", description = "칼로리, 탄수화물, 지방, 단백질 상한선 설정하기")
    public ResponseEntity SetMaxNutrients(@RequestBody NutrientsRequestDto nutrientsRequestDto, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            return userService.setMaxNutrients(nutrientsRequestDto, user.get());
        } else throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);

    }


    @GetMapping("/ingredient")
    @Operation(summary = "사용자 기본정보와 영양정보, 냉장고 속 재료 가져오기", description = "사용자 기본정보와 영양정보, 냉장고 속 재료 가져오기")
    public ResponseEntity GetUserAndIngredient(Authentication authentication) {
        log.info("/ingredient");
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            log.info(user.get().getEmail());
            return userService.getUserAndIngredient(user.get());
        } else throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
    }
}
