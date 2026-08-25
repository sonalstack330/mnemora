package com.mnemora.backend.dto.gemini;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// This class matches the JSON structure Gemini's API expects us to SEND.
// Gemini wants: { "contents": [ { "parts": [ { "text": "..." } ] } ] }
@Getter
@Setter
@AllArgsConstructor
public class GeminiRequest {
    private List<Content> contents;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Content {
        private List<Part> parts;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Part {
        private String text;
    }
}