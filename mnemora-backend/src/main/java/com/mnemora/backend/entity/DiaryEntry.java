package com.mnemora.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

// @Entity tells Spring/Hibernate: "this class maps to a database table"
@Entity
// @Table lets us specify the exact table name (optional, defaults to class name otherwise)
@Table(name = "diary_entries")
// Lombok annotations auto-generate getters, setters, and constructors so we don't write boilerplate
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryEntry {

    // @Id marks this field as the primary key
    @Id
    // Tells the database to auto-increment this value for every new row
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The AI-generated title for this entry
    private String title;

    // The actual diary content (cleaned-up transcript)
    // TEXT type allows long content, unlike default VARCHAR which has a length cap
    @Column(columnDefinition = "TEXT")
    private String content;

    // AI-detected mood, e.g. "happy", "stressed", "neutral"
    private String mood;

    // Comma-separated tags for now, e.g. "college,friends,exam"
    // (we can normalize this into a separate table later if needed)
    private String tags;

    // When this entry was first created
    private LocalDateTime createdAt;

    // When this entry was last edited
    private LocalDateTime updatedAt;

    // Automatically runs right before this entity is first saved to the DB
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    // Automatically runs right before any update to this entity
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}