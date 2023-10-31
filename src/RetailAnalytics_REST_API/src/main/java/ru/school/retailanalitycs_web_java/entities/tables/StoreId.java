package ru.school.retailanalitycs_web_java.entities.tables;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class StoreId implements Serializable {
    @Serial
    private static final long serialVersionUID = 5820011983712050015L;

    @Column(name = "transaction_store_id", nullable = false)
    private Long transactionStoreId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sku_id", nullable = false)
    private Sku sku;

}