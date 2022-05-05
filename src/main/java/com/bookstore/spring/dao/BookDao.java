package com.bookstore.spring.dao;

import com.bookstore.spring.entity.Book;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author VHBin
 * @date 2021/12/17 - 14:58
 */

@Repository
public interface BookDao {
    List<Book> selectAll();

    List<Book> selectByCategoryId(@Param("id") String id);
}
