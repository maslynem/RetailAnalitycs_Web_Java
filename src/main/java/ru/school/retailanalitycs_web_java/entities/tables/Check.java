package ru.school.retailanalitycs_web_java.entities.tables;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@Entity
@ToString
@RequiredArgsConstructor
@Table(name = "checks")
public class Check {
    @EmbeddedId
    private CheckId id;

    @MapsId("transactionId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "transaction_id", nullable = false)
    @ToString.Exclude
    private Transaction transaction;

    @MapsId("skuId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sku_id", nullable = false)
    @ToString.Exclude
    private Sku sku;

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