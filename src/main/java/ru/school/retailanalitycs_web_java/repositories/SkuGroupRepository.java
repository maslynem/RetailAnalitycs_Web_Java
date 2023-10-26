package ru.school.retailanalitycs_web_java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.school.retailanalitycs_web_java.entities.tables.SkuGroup;

import java.util.Optional;

public interface SkuGroupRepository extends JpaRepository<SkuGroup, Integer> {
    Optional<SkuGroup> findByGroupName(String groupName);
}