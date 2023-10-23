package ru.school.retailanalitycs_web_java.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.school.retailanalitycs_web_java.services.BaseService;

import java.util.List;

public abstract class GenericController<E, K> {
    protected final BaseService<E, K> baseService;

    protected GenericController(BaseService<E, K> baseService) {
        this.baseService = baseService;
    }

    @GetMapping
    public List<E> findAllCustomers() {
        return baseService.findAll();
    }

    @GetMapping("all")
    public Page<E> findAllByPage(@RequestParam("page") int page,
                                 @RequestParam("size") int size) {
        return baseService.findAllByPage(page, size);
    }

    @GetMapping("/{id}")
    public E findCustomerById(@PathVariable K id) {
        return baseService.findById(id).orElseThrow(); // todo exception
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public E create(@RequestBody E entity) {
        return baseService.save(entity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") K id) {
        baseService.deleteById(id);
    }

}
