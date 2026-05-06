package com.medinova.dao;

import java.util.List;

public interface BaseDAO<T> {
    void add(T t);
    List<T> getAll();
    void update(T t);
    void delete(int id);
}
