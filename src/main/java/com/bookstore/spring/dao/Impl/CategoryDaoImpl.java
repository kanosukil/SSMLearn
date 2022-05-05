package com.bookstore.spring.dao.Impl;

import com.bookstore.spring.dao.CategoryDao;
import com.bookstore.spring.entity.Category;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author VHBin
 * @date 2021/12/17 - 17:53
 */

public class CategoryDaoImpl extends SqlSessionDaoSupport implements CategoryDao {

    @Override
    public List<Category> selectAll() {
        return this.getSqlSession().selectList("com.bookstore.spring.dao.CategoryDao.selectAll");
    }
}
