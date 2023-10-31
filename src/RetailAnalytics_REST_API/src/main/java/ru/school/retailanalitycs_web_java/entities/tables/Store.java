package ru.school.retailanalitycs_web_java.entities.tables;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "stores")
public class Store implements Serializable {
    @EmbeddedId
    private StoreId id;

    @Column(name = "sku_purchase_price", nullable = false)
    private Double skuPurchasePrice;

    @Column(name = "sku_retail_price", nullable = false)
    private Double skuRetailPrice;

}