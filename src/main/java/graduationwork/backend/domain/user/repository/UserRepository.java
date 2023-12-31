package graduationwork.backend.domain.user.repository;


import graduationwork.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    Optional<User> findByRefreshToken(String refreshToken);
}
