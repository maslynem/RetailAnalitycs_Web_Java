package ru.school.retailanalitycs_web_java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.school.retailanalitycs_web_java.entities.tables.SkuGroup;

public interface GroupsSkuRepository extends JpaRepository<SkuGroup, Integer> {
}