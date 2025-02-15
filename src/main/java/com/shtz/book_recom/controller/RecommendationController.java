package com.shtz.book_recom.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shtz.book_recom.entity.Book;
import com.shtz.book_recom.service.RecommendationService;

@Controller
public class RecommendationController {	
	private final RecommendationService RecommendationService;

    public RecommendationController(RecommendationService RecommendationService) {
        this.RecommendationService = RecommendationService;
        this.RecommendationService.loadBooks(); // Load books on startup
    }

    @GetMapping("/books")
    public String showBooks(Model model) {
    	List<Book> books = RecommendationService.getBooks();
        model.addAttribute("books", books);
        return "books"; // Returns books.html
    }
    
    @GetMapping("/book/{id}")
    public String viewBook(@PathVariable int id, Model model) {
        List<Book> books = RecommendationService.getBooks();
        
        for (Book book : books) {
            if (book.getId() == id) {
                model.addAttribute("book", book);

                // Get recommendations
                List<Book> recommendedBooks = RecommendationService.getRecommendedBooks(book);
                model.addAttribute("recommendedBooks", recommendedBooks);

                return "viewbook"; // Return viewbook.html
            }
        }

        return "redirect:/books"; // Redirect if not found
    }


}
