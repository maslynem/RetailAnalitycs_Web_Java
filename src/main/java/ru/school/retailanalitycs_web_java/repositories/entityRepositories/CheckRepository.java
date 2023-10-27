package ru.school.retailanalitycs_web_java.repositories.entityRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.school.retailanalitycs_web_java.entities.tables.Check;
import ru.school.retailanalitycs_web_java.entities.tables.CheckId;

public interface CheckRepository extends JpaRepository<Check, CheckId> {
}