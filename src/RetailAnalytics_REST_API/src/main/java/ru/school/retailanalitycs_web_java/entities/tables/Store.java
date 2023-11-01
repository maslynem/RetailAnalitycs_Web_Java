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
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "stores")
public class Store implements Serializable {
    @EmbeddedId
    private StoreId id;

    @Column(name = "sku_purchase_price", nullable = false)
    private Double skuPurchasePrice;

    @Column(name = "sku_retail_price", nullable = false)
    private Double skuRetailPrice;

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        Class<?> oEffectiveClass = object instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : object.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Store store = (Store) object;
        return getId() != null && Objects.equals(getId(), store.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}