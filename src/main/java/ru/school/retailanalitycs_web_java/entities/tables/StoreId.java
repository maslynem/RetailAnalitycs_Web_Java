package ru.school.retailanalitycs_web_java.entities.tables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class StoreId implements Serializable {
    @Serial
    private static final long serialVersionUID = 5820011983712050015L;

    @Column(name = "transaction_store_id", nullable = false)
    private Integer transactionStoreId;

    @Column(name = "sku_id", nullable = false)
    private Integer skuId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StoreId entity = (StoreId) o;
        return Objects.equals(this.transactionStoreId, entity.transactionStoreId) &&
                Objects.equals(this.skuId, entity.skuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionStoreId, skuId);
    }

}