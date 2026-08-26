package com.mnemora.backend.controller;

import com.mnemora.backend.dto.TranscriptRequest;
import com.mnemora.backend.dto.DiaryEntryRequest;
import com.mnemora.backend.entity.DiaryEntry;
import com.mnemora.backend.service.DiaryEntryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RestController = @Controller + @ResponseBody combined.
// Tells Spring: "handle HTTP requests here, and automatically convert
// returned Java objects into JSON responses"
@RestController
// Base path — every endpoint in this class starts with /api/entries
@RequestMapping("/api/entries")
public class DiaryEntryController {

    private final DiaryEntryService service;

    @Autowired
    public DiaryEntryController(DiaryEntryService service) {
        this.service = service;
    }

    // POST /api/entries
    // @RequestBody tells Spring to parse the incoming JSON into a DiaryEntryRequest object
    // @Valid triggers the @NotBlank checks we put in the DTO
    @PostMapping
    public ResponseEntity<DiaryEntry> createEntry(@Valid @RequestBody DiaryEntryRequest request) {
        DiaryEntry created = service.createEntry(request);
        // Returns HTTP 201 Created, with the new entry as JSON in the response body
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // GET /api/entries
    @GetMapping
    public ResponseEntity<List<DiaryEntry>> getAllEntries() {
        return ResponseEntity.ok(service.getAllEntries());
    }

    // GET /api/entries/{id}
    // @PathVariable extracts the {id} from the URL, e.g. /api/entries/5 → id = 5
    @GetMapping("/{id}")
    public ResponseEntity<DiaryEntry> getEntryById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getEntryById(id));
    }

    // PUT /api/entries/{id}
    @PutMapping("/{id}")
    public ResponseEntity<DiaryEntry> updateEntry(
            @PathVariable Long id,
            @Valid @RequestBody DiaryEntryRequest request) {
        return ResponseEntity.ok(service.updateEntry(id, request));
    }

    // DELETE /api/entries/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long id) {
        service.deleteEntry(id);
        // 204 No Content — standard response for a successful delete with nothing to return
        return ResponseEntity.noContent().build();
    }

    // GET /api/entries/search?q=keyword
    // @RequestParam extracts ?q=... from the URL query string
    @GetMapping("/search")
    public ResponseEntity<List<DiaryEntry>> searchEntries(@RequestParam String q) {
        return ResponseEntity.ok(service.searchEntries(q));
    }
    // POST /api/entries/from-transcript
    // Takes a raw transcript, runs it through Gemini, saves the structured result
    @PostMapping("/from-transcript")
    public ResponseEntity<DiaryEntry> createFromTranscript(@Valid @RequestBody TranscriptRequest request) {
        DiaryEntry created = service.createEntryFromTranscript(request.getTranscript());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}