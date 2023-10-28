package ru.school.retailanalitycs_web_java.repositories.viewRepositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface ReadOnlyRepository<T, K> extends Repository<T, K> {
    List<T> findAll();

    Page<T> findAll(Pageable pageable);

    Optional<T> findById(K id);
}