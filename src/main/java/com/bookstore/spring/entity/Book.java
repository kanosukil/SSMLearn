package com.bookstore.spring.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author VHBin
 * @date 2021/12/17 - 14:13
 */

@Data
@ToString
public class Book {
    private int id;
    private String name;
    private String author;
    private double price;
    private String image;
    private String description;
    private String category_id;
}
