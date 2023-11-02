package ru.school.retailanalitycs_web_java.entities.tables;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sku")
public class Sku implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sku_id", nullable = false)
    private Long id;

    @Column(name = "sku_name", length = Integer.MAX_VALUE)
    private String skuName;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private SkuGroup skuGroup;

    @OneToMany(mappedBy = "id.sku", orphanRemoval = true)
    @Builder.Default
    private final Set<Store> stores = new LinkedHashSet<>();

    @OneToMany(mappedBy = "id.sku", orphanRemoval = true)
    @Builder.Default
    private final Set<Check> checks = new LinkedHashSet<>();

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        Class<?> oEffectiveClass = object instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : object.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Sku sku = (Sku) object;
        return getId() != null && Objects.equals(getId(), sku.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}