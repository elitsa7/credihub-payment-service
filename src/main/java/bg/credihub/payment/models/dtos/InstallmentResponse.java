package bg.credihub.payment.models.dtos;

import bg.credihub.payment.models.enums.InstallmentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstallmentResponse {
    private UUID id;
    private Integer installmentNumber;
    private BigDecimal amount;
    private LocalDate dueDate;
    private InstallmentStatus status;
    private boolean payable;
}
