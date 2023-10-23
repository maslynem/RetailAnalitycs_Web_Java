package ru.school.retailanalitycs_web_java.entities.tables;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sku")
public class Sku {
    @Id
    @Column(name = "sku_id", nullable = false)
    private Integer id;

    @Column(name = "sku_name", length = Integer.MAX_VALUE)
    private String skuName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private SkuGroup group;

}