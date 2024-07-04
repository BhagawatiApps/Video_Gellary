package com.bhagawatiapps.video_gellary.Activitys;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bhagawatiapps.video_gellary.R;

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class PermissionActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String VIDEO_STORAGE_PERMISSION = Manifest.permission.READ_MEDIA_VIDEO;
    private static final String WRITE_STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    AppCompatButton allowButton, denyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_permission);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find views
        allowButton = findViewById(R.id.allow_button);
        denyButton = findViewById(R.id.Deny_button);

        // Set status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.dark_blue));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.dark_blue));

        // Check if permissions are already granted
        sharedPreferences = getSharedPreferences("permission", MODE_PRIVATE);
        if (sharedPreferences.getString("permission", "").equals("true")) {
            navigateToHomeScreen();
        }

        // Set click listeners
        denyButton.setOnClickListener(v -> {
            editor = sharedPreferences.edit();
            editor.putString("permission", "false");
            editor.apply();
            navigateToHomeScreen();
        });

        allowButton.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                checkPermissions(VIDEO_STORAGE_PERMISSION, WRITE_STORAGE_PERMISSION);
            } else {
                checkPermissions(STORAGE_PERMISSION, WRITE_STORAGE_PERMISSION);
            }
        });
    }

    private void checkPermissions(String... permissions) {
        boolean allPermissionsGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        if (allPermissionsGranted) {
            Toast.makeText(this, "Permissions Already Granted", Toast.LENGTH_SHORT).show();
            editor = sharedPreferences.edit();
            editor.putString("permission", "true");
            editor.apply();
            navigateToHomeScreen();
        } else {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (allPermissionsGranted) {
                Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show();
                editor = sharedPreferences.edit();
                editor.putString("permission", "true");
                editor.apply();
                navigateToHomeScreen();
            } else {
                Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((checkSelfPermission(STORAGE_PERMISSION) == PackageManager.PERMISSION_GRANTED) ||
                (checkSelfPermission(VIDEO_STORAGE_PERMISSION) == PackageManager.PERMISSION_GRANTED) ||
                (checkSelfPermission(WRITE_STORAGE_PERMISSION) == PackageManager.PERMISSION_GRANTED)) {
            navigateToHomeScreen();
        }
    }

    private void navigateToHomeScreen() {
        Intent intent = new Intent(PermissionActivity.this, HomeScreen.class);
        startActivity(intent);
        finish();
    }
}
