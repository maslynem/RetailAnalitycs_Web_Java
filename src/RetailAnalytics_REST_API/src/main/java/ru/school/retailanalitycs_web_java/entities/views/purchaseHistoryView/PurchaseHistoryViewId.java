package ru.school.retailanalitycs_web_java.entities.views.purchaseHistoryView;

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
public class PurchaseHistoryViewId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1475366748096227633L;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "transaction_id")
    private Long transactionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PurchaseHistoryViewId entity = (PurchaseHistoryViewId) o;
        return Objects.equals(this.customerId, entity.customerId) &&
                Objects.equals(this.transactionId, entity.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, transactionId);
    }

}