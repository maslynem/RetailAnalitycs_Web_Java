package ru.school.retailanalitycs_web_java.entities.tables;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "checks")
public class Check {
    @EmbeddedId
    private CheckId id;

    @MapsId("transactionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @MapsId("skuId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sku_id", nullable = false)
    private Sku sku;

    @Column(name = "sku_amount", nullable = false)
    private Double skuAmount;

    @Column(name = "sku_summ", nullable = false)
    private Double skuSumm;

    @Column(name = "sku_summ_paid", nullable = false)
    private Double skuSummPaid;

    @Column(name = "sku_discount", nullable = false)
    private Double skuDiscount;

}