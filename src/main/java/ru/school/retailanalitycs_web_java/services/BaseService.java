package ru.school.retailanalitycs_web_java.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class BaseService<E, K> {
    protected final JpaRepository<E, K> repository;

    protected BaseService(JpaRepository<E, K> repository) {
        this.repository = repository;
    }

    public Page<E> findAllByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

    public E save(E entity) {
        return repository.save(entity);
    }

    public Optional<E> findById(K id) {
        return repository.findById(id);
    }

    public void deleteById(K id) {
        repository.deleteById(id);
    }

    public List<E> findAll() {
        return repository.findAll();
    }

    public void save(Iterable<E> entities) {
        repository.saveAll(entities);
    }
}
