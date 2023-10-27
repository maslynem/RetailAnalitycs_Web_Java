package ru.school.retailanalitycs_web_java.entities.tables;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "personal_data")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id", nullable = false)
    private Long id;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_surname")
    private String customerSurname;

    @Column(name = "customer_primary_email")
    private String customerPrimaryEmail;

    @Column(name = "customer_primary_phone")
    private String customerPrimaryPhone;

    @OneToMany(mappedBy = "customer", orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Card> cards = new LinkedHashSet<>();

}