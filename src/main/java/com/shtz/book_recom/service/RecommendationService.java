package com.shtz.book_recom.service;

import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.shtz.book_recom.entity.Book;

@Service
public class RecommendationService {
	private List<Book> books = new ArrayList<>();

    public void loadBooks() {
        try (CSVReader reader = new CSVReader(new InputStreamReader(new ClassPathResource("book_data.csv").getInputStream()))) {
            String[] line;
            boolean isHeader = true;
            while ((line = reader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                if (line.length < 11) continue;

                try {
                    books.add(new Book(
                        Integer.parseInt(line[0]), // id
                        line[1],                  // title
                        line[2],                  // authors
                        line[3],                  // genre
                        line[4],                  // tags
                        line[5],                  // image
                        line[6],                  // description
                        Integer.parseInt(line[7]),// publishedYear
                        Double.parseDouble(line[8]), // averageRating
                        Integer.parseInt(line[9]), // numPages
                        Double.parseDouble(line[10])  // price
                    ));
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing row: " + String.join(", ", line));
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading books: " + e.getMessage());
        }
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Book> getRecommendedBooks(Book targetBook) {
        if (books == null || books.isEmpty()) {
            return Collections.emptyList(); // Return empty list if no books are available
        }

        return books.stream()
            .filter(b -> b.getId() != targetBook.getId()) // Exclude the current book
            .map(b -> new AbstractMap.SimpleEntry<>(b, computeFinalSimilarity(targetBook, b)))
            .sorted(Comparator.comparingDouble((Map.Entry<Book, Double> entry) -> entry.getValue()).reversed()) // Sort by similarity descending
            .limit(10)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    private double computeFinalSimilarity(Book book1, Book book2) {
        double genreSim = jaccardSimilarity(book1.getGenre(), book2.getGenre());
        double tagSim = cosineSimilarity(book1.getTags(), book2.getTags());
        double authorSim = cosineSimilarity(book1.getAuthor(), book2.getAuthor());

        return (3 * genreSim) + (2 * tagSim) + (1 * authorSim);
    }

    private double jaccardSimilarity(String str1, String str2) {
        Set<String> set1 = new HashSet<>(Arrays.asList(str1.toLowerCase().split(",")));
        Set<String> set2 = new HashSet<>(Arrays.asList(str2.toLowerCase().split(",")));

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return union.isEmpty() ? 0 : (double) intersection.size() / union.size();
    }

    private double cosineSimilarity(String str1, String str2) {
        if (str1.isEmpty() || str2.isEmpty()) return 0.0;

        Map<String, Integer> freqMap1 = getWordFrequencies(str1);
        Map<String, Integer> freqMap2 = getWordFrequencies(str2);

        Set<String> words = new HashSet<>();
        words.addAll(freqMap1.keySet());
        words.addAll(freqMap2.keySet());

        double dotProduct = 0, norm1 = 0, norm2 = 0;

        for (String word : words) {
            int count1 = freqMap1.getOrDefault(word, 0);
            int count2 = freqMap2.getOrDefault(word, 0);

            dotProduct += count1 * count2;
            norm1 += Math.pow(count1, 2);
            norm2 += Math.pow(count2, 2);
        }

        return (norm1 == 0 || norm2 == 0) ? 0.0 : dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    private Map<String, Integer> getWordFrequencies(String text) {
        Map<String, Integer> freqMap = new HashMap<>();
        for (String word : text.toLowerCase().split(",")) {
            freqMap.put(word, freqMap.getOrDefault(word, 0) + 1);
        }
        return freqMap;
    }
}
