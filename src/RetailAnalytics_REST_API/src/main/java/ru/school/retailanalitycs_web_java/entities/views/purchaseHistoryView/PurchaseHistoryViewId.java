package ru.school.retailanalitycs_web_java.entities.views.purchaseHistoryView;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.school.retailanalitycs_web_java.entities.tables.Customer;
import ru.school.retailanalitycs_web_java.entities.tables.Transaction;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PurchaseHistoryViewId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1475366748096227633L;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

}