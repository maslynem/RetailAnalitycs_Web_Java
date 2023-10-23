package ru.school.retailanalitycs_web_java.entities.tables;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "stores")
public class Store {
    @EmbeddedId
    private StoreId id;

    @MapsId("skuId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sku_id", nullable = false)
    private Sku sku;

    @Column(name = "sku_purchase_price", nullable = false)
    private Double skuPurchasePrice;

    @Column(name = "sku_retail_price", nullable = false)
    private Double skuRetailPrice;

}