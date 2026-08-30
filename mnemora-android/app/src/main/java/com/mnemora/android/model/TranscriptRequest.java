package com.mnemora.android.model;

// This mirrors what our backend's TranscriptRequest DTO expects to receive:
// just a single "transcript" field containing the raw speech-to-text output.
public class TranscriptRequest {
    private String transcript;

    public TranscriptRequest(String transcript) {
        this.transcript = transcript;
    }

    public String getTranscript() { return transcript; }
}