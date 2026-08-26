package com.mnemora.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

// This represents the structured output we ask Gemini to produce:
// a title, cleaned content, a mood label, and comma-separated tags.
// We'll ask Gemini to respond in exactly this JSON shape via our prompt.
@Getter
@Setter
@NoArgsConstructor
public class StructuredEntry {
    private String title;
    private String content;
    private String mood;
    private String tags;
}