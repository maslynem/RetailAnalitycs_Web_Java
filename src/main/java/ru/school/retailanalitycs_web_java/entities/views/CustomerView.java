package ru.school.retailanalitycs_web_java.entities.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "customers")
public class CustomerView {
    @Id
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "customer_average_check")
    private Double customerAverageCheck;

    @Column(name = "customer_average_check_segment")
    private String customerAverageCheckSegment;

    @Column(name = "customer_frequency")
    private Double customerFrequency;

    @Column(name = "customer_frequency_segment")
    private String customerFrequencySegment;

    @Column(name = "customer_inactive_period")
    private Double customerInactivePeriod;

    @Column(name = "customer_churn_rate")
    private Double customerChurnRate;

    @Column(name = "customer_churn_segment")
    private String customerChurnSegment;

    @Column(name = "customer_segment")
    private Integer customerSegment;

    @Column(name = "customer_primary_store")
    private Integer customerPrimaryStore;

}