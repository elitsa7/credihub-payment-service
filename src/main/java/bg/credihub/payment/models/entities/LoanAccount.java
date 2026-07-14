package bg.credihub.payment.models.entities;

import bg.credihub.payment.models.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "loan_accounts")
public class LoanAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true)
    private UUID applicationId;
    @Column(nullable = false)
    private UUID userId;
    @Column(nullable = false)
    private BigDecimal principalAmount;
    @Column(nullable = false)
    private BigDecimal remainingBalance;
    @Column(nullable = false)
    private BigDecimal annualInterestRate;
    @Column(nullable = false)
    private BigDecimal monthlyPayment;
    @Column(nullable = false)
    private Integer periodMonths;
    @Column(nullable = false)
    private Integer paidInstallments;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoanStatus status;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @OneToMany(mappedBy = "loanAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("installmentNumber ASC")
    private List<Installment> installments = new ArrayList<>();

}
