package ru.school.retailanalitycs_web_java.repositories.entityRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.school.retailanalitycs_web_java.entities.tables.DateOfAnalysisFormation;

import java.time.LocalDateTime;

public interface DateOfAnalysisFormationRepository extends JpaRepository<DateOfAnalysisFormation, LocalDateTime> {
}