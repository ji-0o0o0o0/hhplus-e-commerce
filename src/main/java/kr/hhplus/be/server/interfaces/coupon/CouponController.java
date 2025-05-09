package kr.hhplus.be.server.interfaces.coupon;

import kr.hhplus.be.server.application.coupon.CouponCommand;
import kr.hhplus.be.server.application.coupon.CouponFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
public class CouponController {

    private final CouponFacade couponFacade;

    @PostMapping("/issue")
    public ResponseEntity<Void> issueCoupon(@RequestBody CouponRequest request) {
        couponFacade.issueCoupon(request.toCommand());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CouponResponse>> getUserCoupons(@PathVariable Long userId) {
        CouponCommand.IssueRequest command = new CouponCommand.IssueRequest(userId, null);
        var result = couponFacade.getUserCoupons(command);
        return ResponseEntity.ok(CouponResponse.listFrom(result.userCoupons()));
    }
}
