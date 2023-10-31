package ru.school.retailanalitycs_web_java.entities.tables;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Entity
@ToString
@RequiredArgsConstructor
@Table(name = "checks")
public class Check implements Serializable {
    @EmbeddedId
    private CheckId id;

    @Column(name = "sku_amount", nullable = false)
    private Double skuAmount;

    @Column(name = "sku_summ", nullable = false)
    private Double skuSum;

    @Column(name = "sku_summ_paid", nullable = false)
    private Double skuSumPaid;

    @Column(name = "sku_discount", nullable = false)
    private Double skuDiscount;

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        Class<?> oEffectiveClass = object instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : object.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Check check = (Check) object;
        return getId() != null && Objects.equals(getId(), check.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}