package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.domain.product.Product;

import java.util.Optional;

public interface UserRepository {
    User findById(Long userId);
}
