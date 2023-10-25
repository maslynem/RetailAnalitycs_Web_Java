package ru.school.retailanalitycs_web_java.entities.tables;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @Column(name = "transaction_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_card_id", nullable = false)
    private Card customerCard;

    @Column(name = "transaction_summ", nullable = false)
    private Double transactionSum;

    @Column(name = "transaction_datetime")
    private LocalDateTime transactionDatetime;

    @Column(name = "transaction_store_id")
    private Integer transactionStoreId;

    @OneToMany(mappedBy = "transaction", orphanRemoval = true)
    private Set<Check> checks = new LinkedHashSet<>();

}