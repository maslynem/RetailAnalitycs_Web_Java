package ru.school.retailanalitycs_web_java.services.entityServices;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public abstract class BaseService<E, K> {
    protected final JpaRepository<E, K> repository;

    protected BaseService(JpaRepository<E, K> repository) {
        this.repository = repository;
    }


    public E save(E entity) {
        return repository.save(entity);
    }

    public void save(Iterable<E> entities) {
        repository.saveAll(entities);
    }

    public void deleteById(K id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<E> findById(K id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<E> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<E> findAllByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }
}
