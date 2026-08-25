package com.mnemora.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// What the client sends when submitting a raw voice transcript
// (before any AI processing has happened)
@Getter
@Setter
public class TranscriptRequest {
    @NotBlank(message = "Transcript is required")
    private String transcript;
}