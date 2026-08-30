package com.mnemora.android.network;

import com.mnemora.android.model.DiaryEntry;
import com.mnemora.android.model.TranscriptRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.POST;

// This interface describes our backend's REST API in Retrofit's language.
// We never implement these methods ourselves - Retrofit generates the
// actual networking code automatically based on these annotations.
public interface MnemoraApiService {

    // Matches: POST /api/entries/from-transcript
    // @Body tells Retrofit to convert our TranscriptRequest object into JSON
    // and send it as the request body
    @POST("api/entries/from-transcript")
    Call<DiaryEntry> createEntryFromTranscript(@Body TranscriptRequest request);

    // Matches: GET /api/entries
    // Returns a list, since this endpoint gives back all diary entries
    @GET("api/entries")
    Call<List<DiaryEntry>> getAllEntries();

    // Matches: GET /api/entries/{id}
    // @Path replaces {id} in the URL with the actual value we pass in
    @GET("api/entries/{id}")
    Call<DiaryEntry> getEntryById(@Path("id") Long id);

    // Matches: DELETE /api/entries/{id}
    @DELETE("api/entries/{id}")
    Call<Void> deleteEntry(@Path("id") Long id);
}