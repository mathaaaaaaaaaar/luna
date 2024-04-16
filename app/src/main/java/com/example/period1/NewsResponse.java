package com.example.period1;

import java.util.List;

public class NewsResponse {
    private String status;
    private int totalResults;
    private List<News> articles;

    // Getters and setters
    public List<News> getArticles() {
        return articles;
    }

    public void setArticles(List<News> articles) {
        this.articles = articles;
    }
}
