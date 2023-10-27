package ru.school.retailanalitycs_web_java.services.entityServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.entities.tables.DateOfAnalysisFormation;
import ru.school.retailanalitycs_web_java.repositories.entityRepositories.DateOfAnalysisFormationRepository;

import java.time.LocalDateTime;

@Service
public class DateOfAnalysisFormationService extends BaseService<DateOfAnalysisFormation, LocalDateTime> {
    @Autowired
    protected DateOfAnalysisFormationService(DateOfAnalysisFormationRepository repository) {
        super(repository);
    }
}
