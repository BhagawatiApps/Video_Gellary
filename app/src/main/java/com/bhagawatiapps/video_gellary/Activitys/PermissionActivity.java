package com.bhagawatiapps.video_gellary.Activitys;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bhagawatiapps.video_gellary.R;

public class PermissionActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    AppCompatButton allow_button, Deny_button;

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

        //find views
        allow_button = findViewById(R.id.allow_button);
        Deny_button = findViewById(R.id.Deny_button);

        //set status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.dark_blue));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.dark_blue));

        //check permission
        sharedPreferences = getSharedPreferences("permission", MODE_PRIVATE);
        String permission = sharedPreferences.getString("permission", "");
        if (permission.equals("true")) {
            Intent intent = new Intent(PermissionActivity.this, HomeScreen.class);
            startActivity(intent);
            finish();
        }


        //set click listeners
        Deny_button.setOnClickListener(v -> {
            editor = sharedPreferences.edit();
            editor.putString("permission", "false");
            editor.apply();
            Intent intent = new Intent(PermissionActivity.this, HomeScreen.class);
            startActivity(intent);
            finish();
        });


        //check permission
        allow_button.setOnClickListener(v -> {
            if (checkSelfPermission(STORAGE_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PermissionActivity.this, HomeScreen.class);
                startActivity(intent);
                finish();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{STORAGE_PERMISSION}, PERMISSION_REQUEST_CODE);
            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                editor = sharedPreferences.edit();
                editor.putString("permission", "true");
                editor.apply();
                Intent intent = new Intent(PermissionActivity.this, HomeScreen.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkSelfPermission(STORAGE_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(PermissionActivity.this, HomeScreen.class);
            startActivity(intent);
            finish();
        }
    }
}