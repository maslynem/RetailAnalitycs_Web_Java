package ru.school.retailanalitycs_web_java.entities.views.purchaseHistoryView;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "purchase_history")
public class PurchaseHistoryView {
    @EmbeddedId
    private PurchaseHistoryViewId id;

    @Column(name = "transaction_datetime")
    private LocalDateTime transactionDatetime;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "group_cost")
    private Double groupCost;

    @Column(name = "group_summ")
    private Double groupSumm;

    @Column(name = "group_summ_paid")
    private Double groupSummPaid;

}