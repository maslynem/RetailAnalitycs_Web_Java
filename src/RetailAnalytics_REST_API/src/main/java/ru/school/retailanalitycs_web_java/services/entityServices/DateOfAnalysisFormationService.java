package ru.school.retailanalitycs_web_java.services.entityServices;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.school.retailanalitycs_web_java.entities.tables.DateOfAnalysisFormation;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.EntityNotFoundException;
import ru.school.retailanalitycs_web_java.repositories.entityRepositories.DateOfAnalysisFormationRepository;

import java.time.LocalDateTime;

@Service
public class DateOfAnalysisFormationService extends BaseService<DateOfAnalysisFormation, LocalDateTime> {

    @Autowired
    protected DateOfAnalysisFormationService(DateOfAnalysisFormationRepository repository, EntityManager entityManager) {
        super(repository, entityManager);
    }

    @Override
    @Transactional
    public DateOfAnalysisFormation update(LocalDateTime id, DateOfAnalysisFormation entity) {
        DateOfAnalysisFormation dateOfAnalysisFormation = findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("DateOfAnalysisFormation with id {%s} not found", id)));
        entity.setId(dateOfAnalysisFormation.getId());
        return merge(entity);
    }
}
