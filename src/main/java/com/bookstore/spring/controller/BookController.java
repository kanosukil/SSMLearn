package com.bookstore.spring.controller;

import com.bookstore.spring.entity.Book;
import com.bookstore.spring.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author VHBin
 * @date 2021/12/19 - 15:06
 */

@Controller
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService service;

    // http://localhost:8080/book?id=1
    @RequestMapping("")
    public @ResponseBody
    List<Book> showBookById(@RequestParam("id")String id) {
        return service.selectByCategoryId(id);
    }

    // http://locahost:8080/book/1
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Book> getBookById(@PathVariable("id")String id) {
        return service.selectByCategoryId(id);
    }

    // http://localhost:8080/book/view?id=1
    @RequestMapping("view")
    public @ResponseBody
    List<Book> findBookById(HttpServletRequest request) {
        String id = request.getParameter("id");
        return service.selectByCategoryId(id);
    }
}
