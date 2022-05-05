package com.bookstore.spring.dao.Impl;

import com.bookstore.spring.dao.BookDao;
import com.bookstore.spring.entity.Book;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author VHBin
 * @date 2021/12/17 - 17:47
 */

public class BookDaoImpl extends SqlSessionDaoSupport implements BookDao {

    @Override
    public List<Book> selectAll() {
        return this.getSqlSession().selectList("com.bookstore.spring.dao.BookDao.selectAll");
    }

    @Override
    public List<Book> selectByCategoryId(String id) {
        return this.getSqlSession().selectList("com.bookstore.spring.dao.BookDao.selectByCategoryId");
    }
}
