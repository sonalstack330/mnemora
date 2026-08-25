package com.mnemora.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// This class represents what the CLIENT sends us when creating/updating an entry.
// We deliberately don't include id, createdAt, updatedAt — those are backend-controlled.
@Getter
@Setter
public class DiaryEntryRequest {

    // @NotBlank means Spring will reject the request with a 400 error
    // if this field is missing or empty — before it even reaches our logic
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    // Optional fields — no validation needed, can be null
    private String mood;
    private String tags;
}