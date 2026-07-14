package bg.credihub.payment.web;

import bg.credihub.payment.models.dtos.InstallmentResponse;
import bg.credihub.payment.service.InstallmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/installments")
public class InstallmentController {
    private final InstallmentService installmentService;

    public InstallmentController(InstallmentService installmentService) {
        this.installmentService = installmentService;
    }

    @GetMapping("/loan/{loanAccountId}")
    public ResponseEntity<List<InstallmentResponse>> getLoanInstallments(@PathVariable UUID loanAccountId) {
        return ResponseEntity.ok(installmentService.getLoanInstallments(loanAccountId));
    }
}
