package kr.hhplus.be.server.infrastructure.User;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public class InMemoryUserRepository implements UserRepository {
    @Override
    public Optional<User> findById(Long UserId) {
        return Optional.empty();
    }
}
