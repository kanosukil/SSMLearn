package com.bookstore.spring.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author VHBin
 * @date 2021/12/17 - 14:38
 */

@Data
@ToString
public class Category {
    private int id;
    private String name;
    private String description;
}
