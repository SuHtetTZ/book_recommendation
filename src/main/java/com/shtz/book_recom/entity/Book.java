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
    private String authors;
    private String genre;
    private String image;
    private String description;
    private int publishedYear;
    private double averageRating;
    private int numPages;

}
