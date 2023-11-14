package graduationwork.backend.domain.user.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpRequestDto {
    private String email;
    private String password;
    private String nickname;
    @Builder
    public SignUpRequestDto(String email, String password,String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
