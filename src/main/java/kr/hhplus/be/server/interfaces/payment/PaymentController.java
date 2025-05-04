package kr.hhplus.be.server.interfaces.payment;

import kr.hhplus.be.server.application.payment.PaymentCommand;
import kr.hhplus.be.server.application.payment.PaymentFacade;
import kr.hhplus.be.server.application.payment.PaymentResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentFacade paymentFacade;

    @PostMapping
    public ResponseEntity<PaymentResponse> pay(@RequestBody PaymentRequest request) {
        PaymentResult result = paymentFacade.pay(request.toCommand());
        return ResponseEntity.ok(PaymentResponse.from(result));
    }
}
