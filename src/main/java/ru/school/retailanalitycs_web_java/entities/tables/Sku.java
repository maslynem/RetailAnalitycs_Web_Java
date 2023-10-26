package ru.school.retailanalitycs_web_java.entities.tables;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "sku")
public class Sku {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sku_id", nullable = false)
    private Integer id;

    @Column(name = "sku_name", length = Integer.MAX_VALUE)
    private String skuName;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private SkuGroup skuGroup;

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