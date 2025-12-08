package com.example.springboot.service;

import com.example.springboot.dao.CourseResourceDao;
import com.example.springboot.entity.CourseResource;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TFIDFclassifierService {

    @Autowired
    private CourseResourceDao courseResourceDao;

    // Store documents (titles) for each category
    private Map<String, List<String>> categoryDocuments = new HashMap<>();

    // Store vocabulary from all documents
    private Set<String> vocabulary = new HashSet<>();

    // Store TF-IDF values for each word in each category
    private Map<String, Map<String, Double>> categoryTfIdfVectors = new HashMap<>();

    // Stop words list
    private Set<String> stopWords = new HashSet<>(Arrays.asList(
            "的", "了", "和", "与", "或", "在", "是", "有", "为", "以", "及", "与", "这", "那", "从", "到"
    ));

    @PostConstruct
    public void init() {
        // Load existing data from database and train model on initialization
        trainModel();
    }

    /**
     * Classify resources based on title
     */
    public String classifyByTitle(String title) {
        if (title == null || title.isEmpty()) {
            return "Other";
        }

        // If model is not trained or insufficient data, use default classifier
        if (categoryTfIdfVectors.isEmpty()) {
            System.out.println("TF-IDF model not trained, using default classifier");
            return "Other";
        }

        // Tokenize title and calculate TF-IDF vector
        Map<String, Double> titleVector = calculateTfIdfVector(tokenize(title.toLowerCase()));

        // Calculate cosine similarity between title and each category
        Map<String, Double> similarities = new HashMap<>();
        for (String category : categoryTfIdfVectors.keySet()) {
            double similarity = calculateCosineSimilarity(titleVector, categoryTfIdfVectors.get(category));
            similarities.put(category, similarity);
            System.out.println("Category " + category + " similarity: " + similarity);
        }

        // Find the category with highest similarity
        return similarities.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Other");
    }

    /**
     * Train TF-IDF model
     */
    public void trainModel() {
        System.out.println("Starting TF-IDF model training...");

        // Reset data structures
        categoryDocuments.clear();
        vocabulary.clear();
        categoryTfIdfVectors.clear();

        // Get all resources from database
        List<CourseResource> allResources = courseResourceDao.selectAll();

        // If insufficient data, skip training
        if (allResources == null || allResources.size() < 10) {
            System.out.println("Insufficient data, cannot effectively train model");
            return;
        }

        // Organize documents by category
        for (CourseResource resource : allResources) {
            String category = resource.getCategory();
            String title = resource.getTitle().toLowerCase();

            if (category == null || category.equals("Other") || title == null || title.isEmpty()) {
                continue;
            }

            categoryDocuments.computeIfAbsent(category, k -> new ArrayList<>()).add(title);

            // Update vocabulary
            vocabulary.addAll(tokenize(title));
        }

        // Calculate TF-IDF vector for each category
        for (String category : categoryDocuments.keySet()) {
            List<String> documents = categoryDocuments.get(category);
            List<List<String>> tokenizedDocs = documents.stream()
                    .map(this::tokenize)
                    .collect(Collectors.toList());

            Map<String, Double> categoryVector = new HashMap<>();

            // Calculate TF-IDF value for each term
            for (String term : vocabulary) {
                double tfIdf = calculateAverageTfIdf(term, tokenizedDocs, allResources.size());
                if (tfIdf > 0) {
                    categoryVector.put(term, tfIdf);
                }
            }

            categoryTfIdfVectors.put(category, categoryVector);
        }

        System.out.println("TF-IDF model training completed, categories: " + categoryTfIdfVectors.size() + ", vocabulary size: " + vocabulary.size());
    }

    /**
     * Calculate average TF-IDF value of a term in documents
     */
    private double calculateAverageTfIdf(String term, List<List<String>> documents, int totalDocCount) {
        double sum = 0.0;
        for (List<String> doc : documents) {
            sum += calculateTf(term, doc) * calculateIdf(term, totalDocCount);
        }
        return sum / documents.size();
    }

    /**
     * Calculate term frequency (TF)
     */
    private double calculateTf(String term, List<String> document) {
        long count = document.stream().filter(term::equals).count();
        return count / (double) document.size();
    }

    /**
     * Calculate inverse document frequency (IDF)
     */
    private double calculateIdf(String term, int totalDocCount) {
        // Calculate number of documents containing this term
        int docCountWithTerm = (int) courseResourceDao.selectAll().stream()
                .filter(resource -> resource.getTitle() != null &&
                        tokenize(resource.getTitle().toLowerCase()).contains(term))
                .count();

        // Prevent division by zero
        if (docCountWithTerm == 0) {
            return 0;
        }

        return Math.log((double) totalDocCount / docCountWithTerm);
    }

    /**
     * Calculate TF-IDF vector for a new title
     */
    private Map<String, Double> calculateTfIdfVector(List<String> tokens) {
        Map<String, Double> vector = new HashMap<>();
        int totalDocCount = courseResourceDao.selectAll().size();

        for (String term : tokens) {
            if (vocabulary.contains(term)) {
                double tf = calculateTf(term, tokens);
                double idf = calculateIdf(term, totalDocCount);
                vector.put(term, tf * idf);
            }
        }

        return vector;
    }

    /**
     * Calculate cosine similarity between two vectors
     */
    private double calculateCosineSimilarity(Map<String, Double> vector1, Map<String, Double> vector2) {
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        // Calculate dot product
        for (String term : vector1.keySet()) {
            if (vector2.containsKey(term)) {
                dotProduct += vector1.get(term) * vector2.get(term);
            }
        }

        // Calculate vector norms
        for (double value : vector1.values()) {
            norm1 += value * value;
        }

        for (double value : vector2.values()) {
            norm2 += value * value;
        }

        // Prevent division by zero
        if (norm1 == 0 || norm2 == 0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    /**
     * Simple Chinese word segmentation implementation, recommend using professional libraries like HanLP, Jieba in actual projects
     */
    private List<String> tokenize(String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyList();
        }


        // 1. First split by punctuation and spaces
        String[] roughTokens = text.replaceAll("[\\p{P}\\s]", " ").split("\\s+");

        List<String> tokens = new ArrayList<>();
        // 2. Filter stop words and add to results
        for (String token : roughTokens) {
            if (!token.isEmpty() && !stopWords.contains(token)) {
                tokens.add(token);
            }
        }

        return tokens;
    }

    /**
     * Public method to retrain model, can be called after adding new resources
     */
    public void retrainModel() {
        trainModel();
    }
}