package ru.school.retailanalitycs_web_java.entities.views.groupView;

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
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class GroupViewId implements Serializable {
    @Serial
    private static final long serialVersionUID = -1663153295275842058L;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "group_id")
    private Long skuGroupId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GroupViewId entity = (GroupViewId) o;
        return Objects.equals(this.skuGroupId, entity.skuGroupId) &&
                Objects.equals(this.customerId, entity.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(skuGroupId, customerId);
    }

}