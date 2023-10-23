package ru.school.retailanalitycs_web_java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.school.retailanalitycs_web_java.entities.tables.Card;

public interface CardRepository extends JpaRepository<Card, Integer> {
}