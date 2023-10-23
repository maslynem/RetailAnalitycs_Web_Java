package ru.school.retailanalitycs_web_java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.school.retailanalitycs_web_java.entities.views.CustomerView;

public interface CustomerViewRepository extends JpaRepository<CustomerView, Integer> {
}