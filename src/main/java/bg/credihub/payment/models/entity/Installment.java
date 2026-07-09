package bg.credihub.payment.models.entity;

import bg.credihub.payment.models.enums.InstallmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "installments")
public class Installment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_account_id", nullable = false)
    private LoanAccount loanAccount;
    @Column(nullable = false)
    private Integer installmentNumber;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    private LocalDate dueDate;
    private LocalDateTime paidAt;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InstallmentStatus status;
}
