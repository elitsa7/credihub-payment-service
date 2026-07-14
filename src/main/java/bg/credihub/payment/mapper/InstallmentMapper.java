package bg.credihub.payment.mapper;

import bg.credihub.payment.models.dtos.InstallmentResponse;
import bg.credihub.payment.models.entities.Installment;
import org.springframework.stereotype.Component;

@Component
public class InstallmentMapper {

    public InstallmentResponse toInstallmentResponse(Installment installment, boolean payable) {
        return InstallmentResponse.builder()
                .id(installment.getId())
                .installmentNumber(installment.getInstallmentNumber())
                .amount(installment.getAmount())
                .dueDate(installment.getDueDate())
                .status(installment.getStatus())
                .payable(payable)
                .build();
    }
}
