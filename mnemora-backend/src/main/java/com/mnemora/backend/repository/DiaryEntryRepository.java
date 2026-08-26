package com.mnemora.backend.repository;

import com.mnemora.backend.entity.DiaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// @Repository marks this as a Spring-managed data access component
@Repository
// Extending JpaRepository<DiaryEntry, Long> automatically gives us:
// save(), findById(), findAll(), deleteById(), and more — no implementation needed!
// DiaryEntry = the entity type this repository manages
// Long = the type of DiaryEntry's primary key (id)
public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, Long> {

    // Spring Data JPA reads this method NAME and auto-generates the SQL query.
    // "findByTitleContainingIgnoreCase" becomes:
    // SELECT * FROM diary_entries WHERE LOWER(title) LIKE LOWER('%keyword%')
    List<DiaryEntry> findByTitleContainingIgnoreCase(String keyword);

    // Same pattern, but searches inside the content field instead
    List<DiaryEntry> findByContentContainingIgnoreCase(String keyword);
}