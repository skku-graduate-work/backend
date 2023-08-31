package graduationwork.backend.domain.user.service;

import graduationwork.backend.domain.user.domain.User;
import graduationwork.backend.domain.user.dto.NutrientsRequestDto;
import graduationwork.backend.domain.user.dto.SignUpRequestDto;
import graduationwork.backend.domain.user.repository.UserRepository;
import graduationwork.backend.global.error.exception.ConflictException;
import graduationwork.backend.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


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
}

