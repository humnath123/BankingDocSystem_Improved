package com.budgettracker.dao;

import com.budgettracker.model.Category;
import java.util.List;

public interface CategoryDao {
    void create(Category category);
    Category findById(int id);
    List<Category> findAll();
    void update(Category category);
    void delete(int id);
}
