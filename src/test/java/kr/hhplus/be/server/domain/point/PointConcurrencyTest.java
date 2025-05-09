package kr.hhplus.be.server.domain.point;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class PointConcurrencyTest {

    @Autowired
    private PointService pointService;

    @Autowired
    private PointRepository pointRepository;

    private final long userId = 1L;

    @BeforeEach
    void clear() {
        pointRepository.deleteAll();
    }

    @Test
    @DisplayName("동시성 테스트: 여러 스레드가 포인트를 동시에 차감")
    void concurrentDecreasePoint_WithExecutorService() throws InterruptedException {
        int threadCount = 10;
        long amount = 200L;

        // 초기 포인트 1000원 설정
        pointRepository.savePoint(Point.of(userId, new BigDecimal(1000)));

        ExecutorService executor = Executors.newFixedThreadPool(10); // 병렬 실행을 위한 스레드풀
        CountDownLatch latch = new CountDownLatch(threadCount); // 모든 작업 완료 대기용
        AtomicInteger successCount = new AtomicInteger(0); // 성공 횟수 추적
        AtomicInteger failCount = new AtomicInteger(0); // 실패 횟수 추적

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    pointService.decrease(userId, BigDecimal.valueOf(amount));
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await(); // 모든 스레드 완료될 때까지 대기

        // 최종 결과 확인
        Point result = pointRepository.findPointByUserId(userId).orElseThrow();
        System.out.println("최종 잔액: " + result.getBalance());

        // 총 시도 수와 성공/실패 수 합이 일치하는지 확인
        assertThat(successCount.get() + failCount.get()).isEqualTo(threadCount);

        //  동시성 제어 미흡한 경우, 잔액이 음수로 내려갈 수 있음 → 테스트 실패해야 정상
        // 과제에서는 명시적으로 실패가 발생하는 상황을 재현하는 것이 목적이므로, 이 테스트는 실패하는 것이 올바름
        assertThat(result.getBalance().longValue()).isBetween(0L, 1000L);
    }

    @Test
    @DisplayName("동시성 테스트: 여러 스레드가 포인트를 동시에 충전")
    void concurrentChargePoint_WithExecutorService() throws InterruptedException {
        int threadCount = 10;
        long chargeAmount = 1000L;

        // 초기 포인트 0원 설정
        pointRepository.savePoint(Point.of(userId, new BigDecimal(0)));

        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    pointService.increase(userId, BigDecimal.valueOf(chargeAmount));
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // 최종 결과 확인
        Point result = pointRepository.findPointByUserId(userId).orElseThrow();
        System.out.println("최종 충전 잔액: " + result.getBalance());

        // 충전은 모두 성공해야 정상이며,
        assertThat(successCount.get()).isEqualTo(threadCount);
        assertThat(failCount.get()).isEqualTo(0);

        //  누락된 쓰기 없이 총합이 정확히 누적되어야 성공
        assertThat(result.getBalance()).isEqualTo(BigDecimal.valueOf(threadCount * chargeAmount));
    }
    @Test
    @DisplayName("동시성 테스트: 여러 유저가 동시에 포인트 충전")
    void concurrentMultipleUsersChargePoint() throws InterruptedException {
        int userCount = 5;
        int threadPerUser = 10;
        long chargeAmount = 1000L;

        CountDownLatch latch = new CountDownLatch(userCount * threadPerUser);
        ExecutorService executor = Executors.newFixedThreadPool(userCount * 2);

        // 각각의 유저에 대해 충전 테스트
        for (long uid = 1; uid <= userCount; uid++) {
            pointRepository.savePoint(Point.of(uid, BigDecimal.ZERO));
            for (int i = 0; i < threadPerUser; i++) {
                final long user = uid;
                executor.submit(() -> {
                    try {
                        pointService.increase(user, BigDecimal.valueOf(chargeAmount));
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }

        latch.await();

        // 각 유저의 충전된 최종 포인트가 예상값과 일치하는지 확인
        for (long uid = 1; uid <= userCount; uid++) {
            Point result = pointRepository.findPointByUserId(uid).orElseThrow();
            assertThat(result.getBalance()).isEqualTo(BigDecimal.valueOf(threadPerUser * chargeAmount));
        }
    }

    @Test
    @DisplayName("동시성 테스트: 한 유저의 충전과 차감이 동시에 발생")
    void concurrentMixedIncreaseAndDecreasePoint() throws InterruptedException {
        int chargeThreadCount = 20;
        int decreaseThreadCount = 20;
        long chargeAmount = 1000L;
        long decreaseAmount = 500L;

        pointRepository.savePoint(Point.of(userId, new BigDecimal(10000)));

        ExecutorService executor = Executors.newFixedThreadPool(chargeThreadCount + decreaseThreadCount);
        CountDownLatch latch = new CountDownLatch(chargeThreadCount + decreaseThreadCount);

        // 충전 스레드 실행
        for (int i = 0; i < chargeThreadCount; i++) {
            executor.submit(() -> {
                try {
                    pointService.increase(userId, BigDecimal.valueOf(chargeAmount));
                } finally {
                    latch.countDown();
                }
            });
        }

        // 차감 스레드 실행
        for (int i = 0; i < decreaseThreadCount; i++) {
            executor.submit(() -> {
                try {
                    pointService.decrease(userId, BigDecimal.valueOf(decreaseAmount));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Point result = pointRepository.findPointByUserId(userId).orElseThrow();
        BigDecimal expected = BigDecimal.valueOf(10000 + (chargeThreadCount * chargeAmount) - (decreaseThreadCount * decreaseAmount));
        assertThat(result.getBalance()).isEqualTo(expected);
    }
}
