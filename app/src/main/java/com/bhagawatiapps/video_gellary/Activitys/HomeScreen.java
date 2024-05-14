package com.bhagawatiapps.video_gellary.Activitys;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bhagawatiapps.video_gellary.Adapter.VideoFolderAdapter;
import com.bhagawatiapps.video_gellary.Model.MediaFiles;
import com.bhagawatiapps.video_gellary.R;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {


    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
    RecyclerView videoFolderRecyclerView;
    ArrayList<MediaFiles> mediaFiles = new ArrayList<>();
    ArrayList<String> videoFolderList = new ArrayList<>();
    VideoFolderAdapter videoFolderAdapter;
    SwipeRefreshLayout refreshFolderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        videoFolderRecyclerView = findViewById(R.id.videoFolderRecyclerView);
        videoFolderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshFolderList = findViewById(R.id.main);
        refreshFolderList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showVideoFolder();
                refreshFolderList.setRefreshing(false);
            }
        });


        // Set status and navigation bar colors
        getWindow().setStatusBarColor(getResources().getColor(R.color.dark_blue));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.dark_blue));


        // Check for storage permission
        if (checkSelfPermission(STORAGE_PERMISSION) == PackageManager.PERMISSION_DENIED) {

            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.permission_dialog);
            dialog.show();
            AppCompatButton allow = dialog.findViewById(R.id.allow);
            AppCompatButton cancel = dialog.findViewById(R.id.cancel);
            allow.setOnClickListener(v -> {
                requestPermissions(new String[]{STORAGE_PERMISSION}, PERMISSION_REQUEST_CODE);
                dialog.dismiss();
            });
            cancel.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }

        showVideoFolder();


    }

    private void showVideoFolder() {
        mediaFiles = loadMediaFiles();

        videoFolderAdapter = new VideoFolderAdapter(this,mediaFiles,videoFolderList);
        videoFolderRecyclerView.setAdapter(videoFolderAdapter);


    }

    private ArrayList<MediaFiles> loadMediaFiles() {
        ArrayList<MediaFiles> mediaFiles1 = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        try {

            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String ID = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                    String TITLE = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE));
                    String DESPLAY_NAME = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                    String SIZE = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));
                    String DURATION = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DURATION));
                    String PATH = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    String DATE_ADDED = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED));

                    MediaFiles files = new MediaFiles(ID, TITLE, DESPLAY_NAME, SIZE, DURATION, PATH, DATE_ADDED);
                    mediaFiles1.add(files);

                    int index = PATH.lastIndexOf("/");
                    String subPath = PATH.substring(0, index);
                    if (!videoFolderList.contains(subPath)) {
                        videoFolderList.add(subPath);
                    }
                    Log.d("TAG", "loadMediaFiles: "+subPath);
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaFiles1;
    }


}