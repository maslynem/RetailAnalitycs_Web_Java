package ru.school.retailanalitycs_web_java.entities.views.groupView;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
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

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        Class<?> oEffectiveClass = object instanceof HibernateProxy ? ((HibernateProxy) object).getHibernateLazyInitializer().getPersistentClass() : object.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        GroupView groupView = (GroupView) object;
        return getId() != null && Objects.equals(getId(), groupView.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}