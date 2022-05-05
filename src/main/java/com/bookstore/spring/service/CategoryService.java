package com.bookstore.spring.service;

import com.bookstore.spring.dao.BookDao;
import com.bookstore.spring.dao.CategoryDao;
import com.bookstore.spring.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author VHBin
 * @date 2021/12/17 - 15:04
 */

@Service
public class CategoryService {
    @Autowired
    private CategoryDao categoryDao;

    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public List<Category> selectAll() {
        return categoryDao.selectAll();
    }

}
