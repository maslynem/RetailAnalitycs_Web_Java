package ru.school.retailanalitycs_web_java.entities.tables;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "date_of_analysis_formation")
public class DateOfAnalysisFormation {
    @Id
    @Column(name = "analysis_formation", nullable = false)
    private Instant id;

    //TODO [JPA Buddy] generate columns from DB
}