package com.shtz.book_recom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.shtz.book_recom.entity.Book;
import com.shtz.book_recom.service.RecommendationService;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
	@Autowired
    private RecommendationService recommendationService;

    @GetMapping("/load")
    public String loadBooks(@RequestParam String filePath) {
        try {
            recommendationService.loadBooks(filePath);
            return "Books loaded successfully!";
        } catch (Exception e) {
            return "Error loading books: " + e.getMessage();
        }
    }

    @GetMapping("/{bookId}")
    public List<Book> getRecommendations(@PathVariable int bookId) {
        return recommendationService.getRecommendations(bookId);
    }

}
