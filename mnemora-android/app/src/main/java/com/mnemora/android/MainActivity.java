package com.mnemora.android;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    // A request code is just an arbitrary number we choose, used to identify
    // WHICH permission request this is when the result comes back
    // (useful if an app requests multiple different permissions)
    private static final int RECORD_AUDIO_PERMISSION_CODE = 100;

    private Button recordButton;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link Java variables to the actual UI elements defined in activity_main.xml,
        // using their android:id values
        recordButton = findViewById(R.id.recordButton);
        statusText = findViewById(R.id.statusText);

        // When the record button is tapped, check/request permission first
        recordButton.setOnClickListener(v -> checkAudioPermissionAndProceed());
    }

    // Checks if we already have mic permission. If yes, proceed (later: start recording).
    // If no, ask the user for it.
    private void checkAudioPermissionAndProceed() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            // We already have permission — for now just show a confirmation.
            // This is where we'll plug in actual SpeechRecognizer logic in the next step.
            statusText.setText("Permission granted. Ready to record (recording logic comes next).");
        } else {
            // We don't have permission yet — ask the user.
            // This triggers the system's built-in permission popup.
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    RECORD_AUDIO_PERMISSION_CODE
            );
        }
    }

    // This method is automatically called by Android after the user responds
    // to the permission popup (whether they tap Allow or Deny)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Confirm this callback is about OUR specific permission request
        if (requestCode == RECORD_AUDIO_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                statusText.setText("Permission granted. Ready to record (recording logic comes next).");
            } else {
                // User denied it - explain why we need it, since without mic access
                // the core feature of this app can't work at all
                Toast.makeText(this,
                        "Microphone permission is required to record diary entries.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}