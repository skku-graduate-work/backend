package graduationwork.backend.domain.user.controller;

import graduationwork.backend.domain.user.domain.User;
import graduationwork.backend.domain.user.repository.UserRepository;
import graduationwork.backend.domain.user.dto.NutrientsRequestDto;
import graduationwork.backend.domain.user.dto.SignUpRequestDto;
import graduationwork.backend.global.error.exception.ErrorCode;
import graduationwork.backend.global.error.exception.UnauthorizedException;
import graduationwork.backend.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @PutMapping("/min-nutrients")
    @Operation(summary = "개인 영양소 하한선 설정",description = "칼로리, 탄수화물, 지방, 단백질 하한선 설정하기")
    public ResponseEntity SetMinNutrients(@RequestBody NutrientsRequestDto nutrientsRequestDto, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            return userService.setMinNutrients(nutrientsRequestDto, user.get());
        } else throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);

    }

    @PutMapping("/max-nutrients")
    @Operation(summary = "개인 영양소 상한선 설정",description = "칼로리, 탄수화물, 지방, 단백질 상한선 설정하기")
    public ResponseEntity SetMaxNutrients(@RequestBody NutrientsRequestDto nutrientsRequestDto, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            return userService.setMaxNutrients(nutrientsRequestDto, user.get());
        } else throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);

    }


}
