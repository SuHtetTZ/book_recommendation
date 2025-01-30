package com.shtz.book_recom.service;


import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.shtz.book_recom.entity.Book;

@Service
public class RecommendationService {
	
	private List<Book> books;

    // Load books from CSV
    public void loadBooks(String filePath) throws Exception {
        books = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            boolean isHeader = true;
            while ((line = reader.readNext()) != null) {
                if (isHeader) { // Skip header
                    isHeader = false;
                    continue;
                }
                books.add(new Book(
                    Integer.parseInt(line[0]), // id
                    line[1],                  // title
                    line[2],                  // authors
                    line[3],                  // genre
                    line[4],                  // image
                    line[5],                  // description
                    Integer.parseInt(line[6]),// publishedYear
                    Double.parseDouble(line[7]), // averageRating
                    Integer.parseInt(line[8]) // numPages
                ));
            }
        }
    }

    // Get recommendations
    public List<Book> getRecommendations(int bookId) {
        // Find the target book
        Book targetBook = books.stream()
                               .filter(b -> b.getId() == bookId)
                               .findFirst()
                               .orElse(null);

        if (targetBook == null) {
            throw new RuntimeException("Book not found");
        }

        String targetGenre = targetBook.getGenre();

        // Calculate similarity based on genre
        Map<Book, Double> similarityScores = new HashMap<>();
        for (Book book : books) {
            if (book.getId() != bookId) {
                double similarity = calculateGenreSimilarity(targetGenre, book.getGenre());
                similarityScores.put(book, similarity);
            }
        }

        // Sort by similarity and return top 5
        return similarityScores.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // Calculate similarity between genres (simple example)
    private double calculateGenreSimilarity(String genre1, String genre2) {
        Set<String> set1 = new HashSet<>(Arrays.asList(genre1.split("\\|")));
        Set<String> set2 = new HashSet<>(Arrays.asList(genre2.split("\\|")));

        // Jaccard Similarity
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }
	
}
