package com.mnemora.android.model;

// This class mirrors the JSON structure returned by our Spring Boot backend's
// DiaryEntry entity. Field names must match exactly (Gson maps by name).
public class DiaryEntry {
    private Long id;
    private String title;
    private String content;
    private String mood;
    private String tags;
    private String createdAt;
    private String updatedAt;

    // Getters - Retrofit/Gson needs these to read values when converting JSON -> object
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getMood() { return mood; }
    public String getTags() { return tags; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
}