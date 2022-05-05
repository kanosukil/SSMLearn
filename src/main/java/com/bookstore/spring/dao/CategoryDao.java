package com.bookstore.spring.dao;

import com.bookstore.spring.entity.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author VHBin
 * @date 2021/12/17 - 15:00
 */

@Repository
public interface CategoryDao {
    List<Category> selectAll();
}
