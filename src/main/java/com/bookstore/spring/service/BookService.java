package com.bookstore.spring.service;

import com.bookstore.spring.dao.BookDao;
import com.bookstore.spring.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author VHBin
 * @date 2021/12/17 - 15:04
 */

@Service
public class BookService {
    @Autowired
    private BookDao bookDao;

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public List<Book> selectAll() {
        return bookDao.selectAll();
    }

    public List<Book> selectByCategoryId(String id) {
        return bookDao.selectByCategoryId(id);
    }

}
