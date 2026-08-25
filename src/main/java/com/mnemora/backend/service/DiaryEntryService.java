package com.mnemora.backend.service;

import com.mnemora.backend.dto.StructuredEntry;
import com.mnemora.backend.exception.EntryNotFoundException;
import com.mnemora.backend.dto.DiaryEntryRequest;
import com.mnemora.backend.entity.DiaryEntry;
import com.mnemora.backend.repository.DiaryEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// @Service marks this as a Spring-managed business logic component
@Service
public class DiaryEntryService {

    private final DiaryEntryRepository repository;
    private final GeminiService geminiService;

    // Constructor injection — Spring automatically gives us an instance
    // of DiaryEntryRepository here. This is the recommended way to inject
    // dependencies (safer and more testable than field injection).
    @Autowired
    public DiaryEntryService(DiaryEntryRepository repository, GeminiService geminiService)
    {
        this.repository = repository;
        this.geminiService = geminiService;
    }

    // CREATE — takes the incoming request DTO, builds a DiaryEntry entity, saves it
    public DiaryEntry createEntry(DiaryEntryRequest request) {
        DiaryEntry entry = new DiaryEntry();
        entry.setTitle(request.getTitle());
        entry.setContent(request.getContent());
        entry.setMood(request.getMood());
        entry.setTags(request.getTags());
        // createdAt/updatedAt are set automatically by @PrePersist in the entity
        return repository.save(entry);
    }

    // READ ALL
    public List<DiaryEntry> getAllEntries() {
        return repository.findAll();
    }

    // READ ONE — throws an exception if not found, which we'll handle in the controller
    public DiaryEntry getEntryById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntryNotFoundException("Entry not found with id: " + id));
    }

    // UPDATE — fetch existing entry, overwrite fields, save again
    public DiaryEntry updateEntry(Long id, DiaryEntryRequest request) {
        DiaryEntry entry = getEntryById(id); // reuse the method above
        entry.setTitle(request.getTitle());
        entry.setContent(request.getContent());
        entry.setMood(request.getMood());
        entry.setTags(request.getTags());
        // updatedAt is refreshed automatically by @PreUpdate in the entity
        return repository.save(entry);
    }

    // DELETE
    public void deleteEntry(Long id) {
        DiaryEntry entry = getEntryById(id); // confirms it exists before deleting
        repository.delete(entry);
    }

    // SEARCH by keyword — searches title OR content
    public List<DiaryEntry> searchEntries(String keyword) {
        List<DiaryEntry> byTitle = repository.findByTitleContainingIgnoreCase(keyword);
        List<DiaryEntry> byContent = repository.findByContentContainingIgnoreCase(keyword);

        // Merge results, avoiding duplicates (in case a keyword matches both title and content)
        byTitle.removeIf(entry -> byContent.contains(entry));
        byTitle.addAll(byContent);
        return byTitle;
    }
    // Takes a raw transcript, sends it through Gemini to get structured fields,
    // then saves it as a new DiaryEntry — this is the core "voice to diary" flow
    public DiaryEntry createEntryFromTranscript(String rawTranscript) {
        StructuredEntry structured = geminiService.structureTranscript(rawTranscript);

        DiaryEntry entry = new DiaryEntry();
        entry.setTitle(structured.getTitle());
        entry.setContent(structured.getContent());
        entry.setMood(structured.getMood());
        entry.setTags(structured.getTags());

        return repository.save(entry);
    }
}