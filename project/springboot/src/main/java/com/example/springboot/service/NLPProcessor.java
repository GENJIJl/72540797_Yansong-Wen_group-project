package com.example.springboot.service;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class NLPProcessor {

    // Load HanLP configuration
    static {
        String root = System.getProperty("user.dir") + "/src/main/resources/hanlp";
        HanLP.Config.enableDebug(false);
        HanLP.Config.CoreDictionaryPath = root + "/dictionary/CoreNatureDictionary.txt";
    }

    /**
     * Chinese word segmentation + remove stop words
     */
    public List<String> segmentAndFilter(String text) {
        List<Term> terms = HanLP.segment(text);
        return terms.stream()
                .map(term -> term.word)
                .filter(word -> !word.matches("[的与和等是了在]")) // Custom stop words
                .collect(Collectors.toList());
    }

    /**
     * Extract noun keywords
     */
    public List<String> extractKeywords(String text) {
        return HanLP.segment(text).stream()
                .filter(term -> term.nature.toString().startsWith("n")) // Keep nouns
                .map(term -> term.word)
                .collect(Collectors.toList());
    }
}