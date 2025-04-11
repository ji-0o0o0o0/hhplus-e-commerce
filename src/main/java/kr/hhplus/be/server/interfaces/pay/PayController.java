package kr.hhplus.be.server.interfaces.pay;

import kr.hhplus.be.server.application.pay.PayCommand;
import kr.hhplus.be.server.application.pay.PayFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/payments")
public class PayController {

    private final PayFacade payFacade;

    @PostMapping
    public ResponseEntity<Void> pay(@RequestBody PayRequest request) {
        PayCommand command = request.toCommand();
        payFacade.pay(command);
        return ResponseEntity.noContent().build();
    }
}
