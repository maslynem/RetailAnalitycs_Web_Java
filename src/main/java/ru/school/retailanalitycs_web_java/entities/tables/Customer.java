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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_generator")
    @SequenceGenerator(name = "customer_id_generator", sequenceName = "personal_data_customer_id_seq", allocationSize = 1)
    @Column(name = "customer_id", nullable = false)
    private Integer id;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_surname")
    private String customerSurname;

    @Column(name = "customer_primary_email")
    private String customerPrimaryEmail;

    @Column(name = "customer_primary_phone")
    private String customerPrimaryPhone;

    @OneToMany(mappedBy = "customer", orphanRemoval = true)
    @ToString.Exclude
    private Set<Card> cards = new LinkedHashSet<>();

}