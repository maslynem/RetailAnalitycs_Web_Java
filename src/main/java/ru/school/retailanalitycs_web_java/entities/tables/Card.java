package ru.school.retailanalitycs_web_java.entities.tables;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "cards")
public class Card {
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_card_id_generator")
//    @SequenceGenerator(name = "customer_card_id_generator", sequenceName = "cards_customer_card_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_card_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "customerCard", orphanRemoval = true)
    private Set<Transaction> transactions = new LinkedHashSet<>();

}