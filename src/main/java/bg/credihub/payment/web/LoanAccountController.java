package bg.credihub.payment.web;

import bg.credihub.payment.models.dtos.LoanAccountRequest;
import bg.credihub.payment.models.dtos.LoanAccountResponse;
import bg.credihub.payment.service.LoanAccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/loan-accounts")
public class LoanAccountController {
    private final LoanAccountService loanAccountService;

    public LoanAccountController(LoanAccountService loanAccountService) {
        this.loanAccountService = loanAccountService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanAccountResponse>> getUserLoans(@PathVariable UUID userId) {
        return ResponseEntity.ok(loanAccountService.getUserLoans(userId));
    }

    @PostMapping
    public ResponseEntity<LoanAccountResponse> createLoanAccount(@Valid @RequestBody LoanAccountRequest request) {
        LoanAccountResponse response = loanAccountService.createLoan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
