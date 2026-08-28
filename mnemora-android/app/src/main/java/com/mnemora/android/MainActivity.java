package com.mnemora.android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int RECORD_AUDIO_PERMISSION_CODE = 100;

    private Button recordButton;
    private TextView statusText;

    // The core object that handles converting speech to text on-device
    private SpeechRecognizer speechRecognizer;

    // The actual instruction/config we send to the recognizer
    private Intent speechRecognizerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recordButton = findViewById(R.id.recordButton);
        statusText = findViewById(R.id.statusText);

        setupSpeechRecognizer();

        recordButton.setOnClickListener(v -> checkAudioPermissionAndProceed());
    }

    // Sets up the SpeechRecognizer object and defines what it does at each stage
    // of listening (ready, hearing speech, done, error, etc.)
    private void setupSpeechRecognizer() {
        // Creates the recognizer instance tied to this activity
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        // Configures the recognition request:
        // - LANGUAGE_MODEL_FREE_FORM: optimized for natural speech (diary entries),
        //   not short commands
        // - EXTRA_LANGUAGE: English, can be changed/made dynamic later
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        // RecognitionListener defines callback methods for each stage of the
        // listening lifecycle - Android calls these automatically as speech happens
        speechRecognizer.setRecognitionListener(new RecognitionListener() {

            @Override
            public void onReadyForSpeech(Bundle params) {
                // Called when the recognizer is ready and actively listening
                statusText.setText("Listening... speak now");
            }

            @Override
            public void onBeginningOfSpeech() {
                // Called the moment the user starts speaking
                statusText.setText("Listening...");
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                // Called repeatedly with volume level changes - useful later for
                // a live waveform animation, not needed for MVP
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                // Raw audio buffer - not needed since we're using the built-in
                // recognizer rather than processing audio ourselves
            }

            @Override
            public void onEndOfSpeech() {
                // Called when the user stops speaking and processing begins
                statusText.setText("Processing speech...");
            }

            @Override
            public void onError(int error) {
                // Called if something goes wrong (no speech detected, network issue, etc.)
                statusText.setText("Error occurred, please try again");
                recordButton.setEnabled(true);
            }

            @Override
            public void onResults(Bundle results) {
                // This is the important one - called when transcription succeeds.
                // Results come back as a list of possible matches, ranked by confidence.
                ArrayList<String> matches = results.getStringArrayList(
                        SpeechRecognizer.RESULTS_RECOGNITION);

                if (matches != null && !matches.isEmpty()) {
                    // Take the top (most confident) match
                    String transcript = matches.get(0);
                    statusText.setText("Transcript: " + transcript);

                    // This is where we'll send the transcript to our backend's
                    // /api/entries/from-transcript endpoint - next step
                } else {
                    statusText.setText("No speech detected, try again");
                }

                recordButton.setEnabled(true);
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                // Called with interim results while still listening -
                // not needed for MVP, but could show live text later
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                // Reserved for future/vendor-specific events - not needed here
            }
        });
    }

    private void checkAudioPermissionAndProceed() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            startListening();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    RECORD_AUDIO_PERMISSION_CODE
            );
        }
    }

    // Actually starts the speech recognition session
    private void startListening() {
        recordButton.setEnabled(false); // prevent double-taps while listening
        speechRecognizer.startListening(speechRecognizerIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RECORD_AUDIO_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startListening();
            } else {
                Toast.makeText(this,
                        "Microphone permission is required to record diary entries.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Always release the recognizer when the activity is destroyed,
        // to free up system resources properly
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }
}