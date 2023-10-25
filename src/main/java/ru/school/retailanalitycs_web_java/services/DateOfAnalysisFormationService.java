package ru.school.retailanalitycs_web_java.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.entities.tables.DateOfAnalysisFormation;

import java.time.LocalDateTime;

@Service
public class DateOfAnalysisFormationService extends BaseService<DateOfAnalysisFormation, LocalDateTime> {
    @Autowired
    protected DateOfAnalysisFormationService(JpaRepository<DateOfAnalysisFormation, LocalDateTime> repository) {
        super(repository);
    }
}
