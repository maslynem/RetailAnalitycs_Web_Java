package ru.school.retailanalitycs_web_java.repositories.entityRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.school.retailanalitycs_web_java.entities.tables.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}