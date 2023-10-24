package ru.school.retailanalitycs_web_java.services;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.school.retailanalitycs_web_java.entities.tables.DateOfAnalysisFormation;

import java.time.LocalDateTime;

public class DateOfAnalysisFormationService extends BaseService<DateOfAnalysisFormation, LocalDateTime> {
    protected DateOfAnalysisFormationService(JpaRepository<DateOfAnalysisFormation, LocalDateTime> repository) {
        super(repository);
    }
}
