package ru.school.retailanalitycs_web_java.services.viewServices;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.school.retailanalitycs_web_java.repositories.viewRepositories.ReadOnlyRepository;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public abstract class ReadOnlyBaseService<E, K> {
    protected final ReadOnlyRepository<E, K> repository;

    protected ReadOnlyBaseService(ReadOnlyRepository<E, K> repository) {
        this.repository = repository;
    }

    public List<E> findAll() {
        return repository.findAll();
    }

    public Optional<E> findById(K id) {
        return repository.findById(id);
    }

    public Page<E> findAllByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

}
