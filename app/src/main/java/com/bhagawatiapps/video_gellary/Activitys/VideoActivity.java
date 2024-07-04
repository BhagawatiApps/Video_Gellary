package com.bhagawatiapps.video_gellary.Activitys;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhagawatiapps.video_gellary.Adapter.VideoFilesAdapter;
import com.bhagawatiapps.video_gellary.Model.MediaFiles;
import com.bhagawatiapps.video_gellary.R;

import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    VideoFilesAdapter videoFilesAdapter;
    private ArrayList<MediaFiles> videoFilesArrayList = new ArrayList<>();
    private String folderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Intent intent = getIntent();
        folderName = intent.getStringExtra("folderName");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(folderName);
        // Set status and navigation bar colors
        getWindow().setStatusBarColor(getResources().getColor(R.color.dark_blue));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.dark_blue));

        recyclerView = findViewById(R.id.videos_rv);
        showVideosFiles();


    }

    private void showVideosFiles() {
        videoFilesArrayList = fetchVideoFiles(folderName);
        videoFilesAdapter = new VideoFilesAdapter(this, videoFilesArrayList);
        recyclerView.setAdapter(videoFilesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        videoFilesAdapter.notifyDataSetChanged();
    }

    private ArrayList<MediaFiles> fetchVideoFiles(String folderName) {
        ArrayList<MediaFiles> videoFiles = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Video.Media.DATA + " LIKE ?";

        String[] selectionArgs = new String[]{"%" + folderName + "%"};
        try {
            Cursor cursor = getContentResolver().query(uri, null, selection, selectionArgs, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String ID = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                    String TITLE = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE));
                    String DESPLAY_NAME = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                    String SIZE = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));
                    String DURATION = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DURATION));
                    String PATH = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    String DATE_ADDED = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED));

                    MediaFiles mediaFiles = new MediaFiles(ID, TITLE, DESPLAY_NAME, SIZE, DURATION, PATH, DATE_ADDED);
                    videoFiles.add(mediaFiles);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoFiles;
    }
}