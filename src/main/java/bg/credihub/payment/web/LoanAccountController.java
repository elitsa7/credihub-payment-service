package bg.credihub.payment.web;

import bg.credihub.payment.models.dto.LoanAccountRequest;
import bg.credihub.payment.models.dto.LoanAccountResponse;
import bg.credihub.payment.service.LoanAccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/loan-accounts")
public class LoanAccountController {
    private final LoanAccountService loanAccountService;

    public LoanAccountController(LoanAccountService loanAccountService) {
        this.loanAccountService = loanAccountService;
    }

    @PostMapping
    public ResponseEntity<LoanAccountResponse> createLoanAccount(@Valid @RequestBody LoanAccountRequest request) {
        LoanAccountResponse loanAccountResponse = loanAccountService.createLoan(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
