package ru.school.retailanalitycs_web_java.entities.views.groupView;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "groups")
public class GroupView {
    @EmbeddedId
    private GroupViewId id;

    @Column(name = "group_affinity_index")
    private Double groupAffinityIndex;

    @Column(name = "group_churn_rate")
    private Double groupChurnRate;

    @Column(name = "group_stability_index")
    private Double groupStabilityIndex;

    @Column(name = "group_margin")
    private Double groupMargin;

    @Column(name = "group_discount_share")
    private Double groupDiscountShare;

    @Column(name = "group_minimum_discount")
    private Double groupMinimumDiscount;

    @Column(name = "group_average_discount")
    private Double groupAverageDiscount;

}