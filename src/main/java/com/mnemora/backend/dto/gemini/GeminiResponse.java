package com.mnemora.backend.dto.gemini;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// This class matches the JSON structure Gemini's API SENDS BACK.
// Gemini returns: { "candidates": [ { "content": { "parts": [ { "text": "..." } ] } } ] }
// We only map the fields we actually need — Gemini's real response has more,
// but we can ignore fields we don't use.
@Getter
@Setter
public class GeminiResponse {
    private List<Candidate> candidates;

    @Getter
    @Setter
    public static class Candidate {
        private Content content;
    }

    @Getter
    @Setter
    public static class Content {
        private List<Part> parts;
    }

    @Getter
    @Setter
    public static class Part {
        private String text;
    }
}