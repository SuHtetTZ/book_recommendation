package com.shtz.book_recom.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
	private int id;
    private String title;
    private String author;
    private String genre;
    private String tags;
    private String img;
    private String description;
    private int published_year;
    private double average_rating;
    private int num_pages;
    private double price;

}
