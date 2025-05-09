package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

@SpringBootTest
@ActiveProfiles("test")
public class CouponConcurrencyTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Coupon coupon;

    @BeforeEach
    void setup() {
        user = userRepository.save(Instancio.of(User.class)
                .supply(field(User::getId), () -> null)
                .create());

        coupon = createValidCoupon("테스트쿠폰", 10L);
        couponRepository.save(coupon);

    }

    private Coupon createValidCoupon(String name, long stock) {
        return Coupon.of(
                name,
                new BigDecimal("1000"),
                DiscountType.AMOUNT,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(3),
                stock
        );
    }

    @Test
    @DisplayName("동시성 테스트: 한 유저에게 쿠폰이 중복 발급되지 않도록 막아야 한다")
    void concurrentIssueCoupon_ToSameUser() throws InterruptedException {
        int threadCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    couponService.issueCouponTo(user,coupon.getId());
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        long couponCount = couponRepository.findByCouponId(coupon.getId()).get().getStock();
        assertThat(couponCount).isEqualTo(1);
    }
}
