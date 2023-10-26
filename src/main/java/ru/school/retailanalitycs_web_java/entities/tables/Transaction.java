package ru.school.retailanalitycs_web_java.entities.tables;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@ToString
@RequiredArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_generator")
//    @SequenceGenerator(name = "transaction_id_generator", sequenceName = "transactions_transaction_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_card_id", nullable = false)
    @ToString.Exclude
    private Card customerCard;

    @Column(name = "transaction_summ", nullable = false)
    private Double transactionSum;

    @Column(name = "transaction_datetime", columnDefinition = "TIMESTAMP")
    private LocalDateTime transactionDatetime;

    @Column(name = "transaction_store_id")
    private Integer transactionStoreId;

    @OneToMany(mappedBy = "transaction", orphanRemoval = true)
    @ToString.Exclude
    private Set<Check> checks = new LinkedHashSet<>();

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        Class<?> oEffectiveClass = object instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : object.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Transaction that = (Transaction) object;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hibernateProxy ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}