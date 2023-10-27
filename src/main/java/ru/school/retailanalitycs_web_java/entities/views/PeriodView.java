package ru.school.retailanalitycs_web_java.entities.views;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "periods")
public class PeriodView {
    @EmbeddedId
    private PeriodViewId id;

    @Column(name = "first_group_purchase_date")
    private LocalDateTime firstGroupPurchaseDate;

    @Column(name = "last_group_purchase_date")
    private LocalDateTime lastGroupPurchaseDate;

    @Column(name = "group_purchase")
    private Long groupPurchase;

    @Column(name = "group_frequency")
    private Double groupFrequency;

    @Column(name = "group_min_discount")
    private Double groupMinDiscount;

}