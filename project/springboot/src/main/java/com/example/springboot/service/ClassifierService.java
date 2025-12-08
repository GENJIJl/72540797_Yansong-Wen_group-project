package com.example.springboot.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClassifierService {

    private final Map<String, List<String>> categoryKeywords = new HashMap<>();
    private final Map<String, Map<String, Double>> categoryTfIdfVectors = new HashMap<>();
    private Set<String> allKeywords = new HashSet<>();

    @PostConstruct
    public void init() {
        // Keywords
        categoryKeywords.put("1", Arrays.asList("Chapter 1", "Development Environment", "Installation", "Overview", "Basics", "Member Functions", "What is a Program", "Programmer", "Type Conversion", "Expression", "Comment"));
        categoryKeywords.put("2", Arrays.asList("Chapter 2", "Input", "Output", "Type", "Destructor", "Base Conversion", "scanf", "Mixed Operations", "Integer Base Conversion"));
        categoryKeywords.put("3", Arrays.asList("Chapter 3", "Operators", "Expression", "Recursion", "Inline Functions", "Function Overloading", "Default Parameters", "Scope", "Function Pointers", "Lambda Expression"));
        categoryKeywords.put("4", Arrays.asList("Chapter 4", "Selection", "Loop", "Const Members", "Header Guard", "Namespace", "extern", "constexpr", "Access Control", "Compilation Unit"));
        categoryKeywords.put("5", Arrays.asList("Chapter 5", "One-dimensional Array", "Dynamic Array", "Character Array", "Pointers and References", "Two-dimensional Array", "Const Pointer", "String Stream"));
        categoryKeywords.put("6", Arrays.asList("Chapter 6", "Pointers", "Access Rights", "Multiple Inheritance", "Virtual Base Class", "Override", "final", "Subclass Parent Class", "Inheritance Hierarchy", "Diamond Inheritance"));
        categoryKeywords.put("7", Arrays.asList("Chapter 7", "Functions", "Abstract Class", "override", "Virtual Destructor", "typeid", "dynamic_cast", "Pure Virtual Function", "Runtime Type Identification"));
        categoryKeywords.put("8", Arrays.asList("Chapter 8", "Structure", "Generic Programming", "Template Specialization", "Container Class", "Linked List", "Stack Queue Implementation", "Template Parameters", "Type Deduction", "Variadic Template"));
        categoryKeywords.put("9", Arrays.asList("Chapter 9", "map", "list", "queue", "stack", "algorithm", "Iterator", "set", "multimap", "Adapter", "Function Object", "STL Algorithm", "Iterator Category"));
        categoryKeywords.put("10", Arrays.asList("Chapter 10", "throw", "Exception Specification", "noexcept", "Standard Exception Class", "Exception Safety", "Stack Unwinding", "Custom Exception", "Exception Propagation", "Resource Leak Prevention"));

        // Collect all unique keywords
        categoryKeywords.values().forEach(keywords -> allKeywords.addAll(keywords));

        calculateTfIdfVectors();
    }

    // Calculate TF-IDF vector for each category
    private void calculateTfIdfVectors() {
        int totalCategories = categoryKeywords.size();

        // Calculate document frequency (DF) - how many categories contain each word
        Map<String, Integer> keywordDocFreq = new HashMap<>();
        for (String keyword : allKeywords) {
            int docFreq = 0;
            for (List<String> keywords : categoryKeywords.values()) {
                if (keywords.contains(keyword)) {
                    docFreq++;
                }
            }
            keywordDocFreq.put(keyword, docFreq);
        }

        // Calculate TF-IDF vector for each category
        for (Map.Entry<String, List<String>> entry : categoryKeywords.entrySet()) {
            String category = entry.getKey();
            List<String> keywords = entry.getValue();

            // Calculate term frequency (TF)
            Map<String, Double> termFreq = new HashMap<>();
            for (String keyword : keywords) {
                termFreq.put(keyword, termFreq.getOrDefault(keyword, 0.0) + 1.0);
            }

            // Convert to TF-IDF
            Map<String, Double> tfidfVector = new HashMap<>();
            for (Map.Entry<String, Double> tfEntry : termFreq.entrySet()) {
                String keyword = tfEntry.getKey();
                double tf = tfEntry.getValue() / keywords.size();
                double idf = Math.log((double) totalCategories / keywordDocFreq.get(keyword));
                tfidfVector.put(keyword, tf * idf);
            }

            categoryTfIdfVectors.put(category, tfidfVector);
        }
    }

    public String classifyByTitle(String title) {
        System.out.println("Input title: " + title);

        // Convert title to lowercase and tokenize (simple tokenization, split by spaces)
        String[] titleWords = title.toLowerCase().split("\\s+");

        // Build TF-IDF vector for the title
        Map<String, Double> titleVector = new HashMap<>();
        for (String word : titleWords) {
            // Only consider predefined keywords
            if (allKeywords.contains(word)) {
                titleVector.put(word, titleVector.getOrDefault(word, 0.0) + 1.0);
            }
        }

        // Also check if title contains complete keywords (some keywords may be phrases)
        String titleLower = title.toLowerCase();
        for (String keyword : allKeywords) {
            if (titleLower.contains(keyword.toLowerCase()) && !titleVector.containsKey(keyword)) {
                titleVector.put(keyword, 1.0);
                System.out.println("Matched keyword phrase: " + keyword);
            }
        }

        // If no keywords matched, return default category
        if (titleVector.isEmpty()) {
            return "Other";
        }

        // Calculate cosine similarity between title and each category
        Map<String, Double> similarities = new HashMap<>();
        for (Map.Entry<String, Map<String, Double>> entry : categoryTfIdfVectors.entrySet()) {
            String category = entry.getKey();
            Map<String, Double> categoryVector = entry.getValue();

            double similarity = calculateCosineSimilarity(titleVector, categoryVector);
            if (similarity > 0) {
                similarities.put(category, similarity);
                System.out.println("Category " + category + " similarity: " + similarity);
            }
        }

        // Find the most similar category
        return similarities.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Other");
    }

    // Calculate cosine similarity between two vectors
    private double calculateCosineSimilarity(Map<String, Double> vector1, Map<String, Double> vector2) {
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        // Calculate dot product
        for (Map.Entry<String, Double> entry : vector1.entrySet()) {
            String key = entry.getKey();
            double value1 = entry.getValue();

            if (vector2.containsKey(key)) {
                double value2 = vector2.get(key);
                dotProduct += value1 * value2;
            }
        }

        // Calculate vector norms
        for (double value : vector1.values()) {
            norm1 += value * value;
        }
        for (double value : vector2.values()) {
            norm2 += value * value;
        }

        norm1 = Math.sqrt(norm1);
        norm2 = Math.sqrt(norm2);

        // Avoid division by zero
        if (norm1 == 0 || norm2 == 0) {
            return 0.0;
        }

        return dotProduct / (norm1 * norm2);
    }
}