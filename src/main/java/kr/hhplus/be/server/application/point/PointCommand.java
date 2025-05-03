package kr.hhplus.be.server.application.point;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class PointCommand {

    public record UserIdRequest(
            @NotNull(message = "userId는 필수입니다.")
            @Positive(message = "userId는 0보다 커야 합니다.")
            long userId
    ) {}

    public record IncreaseRequest(
            @NotNull(message = "userId는 필수입니다.")
            @Positive(message = "userId는 0보다 커야 합니다.")
            long userId,

            @NotNull(message = "amount는 필수입니다.")
            @Positive(message = "amount는 0보다 커야 합니다.")
            BigDecimal amount
    ) {}

    public record DecreaseRequest(
            @NotNull(message = "userId는 필수입니다.")
            @Positive(message = "userId는 0보다 커야 합니다.")
            long userId,

            @NotNull(message = "amount는 필수입니다.")
            @Positive(message = "amount는 0보다 커야 합니다.")
            BigDecimal amount
    ) {}
}
