package ru.school.retailanalitycs_web_java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.school.retailanalitycs_web_java.entities.tables.DateOfAnalysisFormation;

import java.time.Instant;

public interface DateOfAnalysisFormationRepository extends JpaRepository<DateOfAnalysisFormation, Instant> {
}