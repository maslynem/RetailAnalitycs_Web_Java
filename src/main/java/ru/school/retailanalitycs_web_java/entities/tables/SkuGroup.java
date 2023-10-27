package ru.school.retailanalitycs_web_java.entities.tables;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "groups_sku")
public class SkuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", nullable = false)
    private Long id;

    @Column(name = "group_name", length = Integer.MAX_VALUE, unique = true)
    private String groupName;

    @OneToMany(mappedBy = "skuGroup", orphanRemoval = true)
    private Set<Sku> skus = new LinkedHashSet<>();

}