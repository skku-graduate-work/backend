package graduationwork.backend.domain.user.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpRequestDto {
    private String email;
    private String password;

    @Builder
    public SignUpRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
