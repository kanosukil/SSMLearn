package com.bookstore.spring.controller;

import com.bookstore.spring.entity.Category;
import com.bookstore.spring.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author VHBin
 * @date 2021/12/17 - 22:50
 */

@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService service;

    @RequestMapping("/view")
    public String viewCategory(Model model) {
        List<Category> list = service.selectAll();
        model.addAttribute("category", list);
        return "main";
    }

    @RequestMapping("/modelView")
    public ModelAndView showCategory(ModelAndView model) {
        List<Category> list = service.selectAll();
        model.setViewName("main");
        model.addObject("category", list);
        return model;
    }

    @RequestMapping("/ID")
    public String viewID(Model model) {
        return "showID";
    }
}
