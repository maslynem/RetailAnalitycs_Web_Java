package ru.school.retailanalitycs_web_java.entities.views.periodView;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PeriodViewId implements Serializable {
    @Serial
    private static final long serialVersionUID = -2908880119812198498L;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "group_id")
    private Long skuGroupId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PeriodViewId entity = (PeriodViewId) o;
        return Objects.equals(this.skuGroupId, entity.skuGroupId) &&
                Objects.equals(this.customerId, entity.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(skuGroupId, customerId);
    }

}