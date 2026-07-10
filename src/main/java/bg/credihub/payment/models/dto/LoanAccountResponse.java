package bg.credihub.payment.models.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class LoanAccountResponse {
    private UUID loanAccountId;
}
