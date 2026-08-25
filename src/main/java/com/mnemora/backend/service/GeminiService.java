package com.mnemora.backend.service;

import tools.jackson.databind.ObjectMapper;
import com.mnemora.backend.dto.StructuredEntry;
import com.mnemora.backend.dto.gemini.GeminiRequest;
import com.mnemora.backend.dto.gemini.GeminiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class GeminiService {

    // @Value pulls values from application.properties into these fields
    // when Spring creates this bean
    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    // RestClient is Spring's modern HTTP client for calling external APIs
    private final RestClient restClient = RestClient.create();

    // Jackson's ObjectMapper converts between Java objects and JSON strings
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Takes a raw transcript, sends it to Gemini with instructions,
    // and returns a structured diary entry
    public StructuredEntry structureTranscript(String rawTranscript) {

        // This is the PROMPT — the instruction we give Gemini.
        // We're explicitly telling it to respond ONLY with JSON, in an exact shape,
        // so we can reliably parse it back into our StructuredEntry class.
        String prompt = """
                You are helping organize a personal voice diary entry.
                Given the raw spoken transcript below, produce a JSON object with
                exactly these fields: title, content, mood, tags.

                - title: a short descriptive title (a few words)
                - content: the transcript cleaned up with proper punctuation and grammar,
                  but keeping the original meaning and first-person voice
                - mood: a single word describing the emotional tone (e.g. happy, stressed, calm, excited, sad)
                - tags: 2-4 comma-separated relevant keywords (e.g. "college,friends,exam")

                Respond with ONLY the JSON object, no extra text, no markdown formatting, no code fences.

                Transcript:
                %s
                """.formatted(rawTranscript);

        // Build the request body matching Gemini's expected structure
        GeminiRequest request = new GeminiRequest(
                List.of(new GeminiRequest.Content(
                        List.of(new GeminiRequest.Part(prompt))
                ))
        );

        // Make the actual HTTP POST call to Gemini's API
        GeminiResponse response = restClient.post()
                .uri(apiUrl + "?key=" + apiKey)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(GeminiResponse.class);

        // Extract the actual text Gemini generated (buried a few levels deep in the response)
        String rawJsonText = response.getCandidates().get(0).getContent().getParts().get(0).getText();

        // Gemini sometimes wraps JSON in markdown code fences (```json ... ```) despite our instructions.
        // Strip those out defensively before parsing.
        String cleanedJson = rawJsonText.replace("```json", "").replace("```", "").trim();

        try {
            // Parse the cleaned JSON string into our StructuredEntry Java object
            return objectMapper.readValue(cleanedJson, StructuredEntry.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Gemini response as structured entry: " + cleanedJson, e);
        }
    }
}