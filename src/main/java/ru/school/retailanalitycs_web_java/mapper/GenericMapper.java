package ru.school.retailanalitycs_web_java.mapper;

public abstract class GenericMapper<E, D> {

    public abstract D toDto(E entity);

    public abstract E toEntity(D dto);
}