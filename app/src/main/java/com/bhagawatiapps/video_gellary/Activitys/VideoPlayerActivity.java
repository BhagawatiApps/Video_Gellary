package com.bhagawatiapps.video_gellary.Activitys;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DefaultDataSourceFactory;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.ConcatenatingMediaSource;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.ProgressiveMediaSource;
import androidx.media3.ui.PlayerView;

import com.bhagawatiapps.video_gellary.Model.MediaFiles;
import com.bhagawatiapps.video_gellary.R;

import java.io.File;
import java.util.ArrayList;

@UnstableApi
public class VideoPlayerActivity extends AppCompatActivity {

    ArrayList<MediaFiles> mVideoFiles = new ArrayList<>();
    PlayerView playerView;
    ExoPlayer exoPlayer;
    int Position;
    String Video_Title;
    TextView Title;
    ImageView nextBtn, prevBtn;
    ConcatenatingMediaSource concatenatingMediaSource;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setFullScreen();
        setContentView(R.layout.activity_video_player);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nextBtn = findViewById(R.id.exo_next);
        prevBtn = findViewById(R.id.exo_prev);
        playerView = findViewById(R.id.player_view);
        Title = findViewById(R.id.Video_Title);
        Position = getIntent().getIntExtra("position", 1);
        Video_Title = getIntent().getStringExtra("videoTitle");
        mVideoFiles = getIntent().getExtras().getParcelableArrayList("videoList");

        Title.setText(Video_Title);

        playVideo();

        nextBtn.setOnClickListener(v -> {
            Position++;
            if (Position <= mVideoFiles.size()) {
                exoPlayer.stop();
                playVideo();
            } else {
                Toast.makeText(this, "No Next Video Available", Toast.LENGTH_SHORT).show();
            }
        });

        prevBtn.setOnClickListener(v -> {
            Position--;
            if (Position >= 0) {
                exoPlayer.stop();
                playVideo();
            } else {
                Position++;
                Toast.makeText(this, "No Previous Video Available", Toast.LENGTH_SHORT).show();
            }


        });


    }

    private void playVideo() {

        String path = mVideoFiles.get(Position).getPath();
        Uri uri = Uri.parse(path);
        exoPlayer = new ExoPlayer.Builder(this).build();
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "app"));

        concatenatingMediaSource = new ConcatenatingMediaSource();
        for (int i = 0; i < mVideoFiles.size(); i++) {
            new File(String.valueOf(mVideoFiles.get(i)));
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(Uri.parse(String.valueOf(uri))));
            concatenatingMediaSource.addMediaSource(mediaSource);
        }
        playerView.setPlayer(exoPlayer);

        playerView.setKeepScreenOn(true);
        exoPlayer.prepare(concatenatingMediaSource);
        exoPlayer.seekTo(Position, C.TIME_UNSET);


        playError();

    }

    private void playError() {
        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlayerError(PlaybackException error) {
                Toast.makeText(VideoPlayerActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        exoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (exoPlayer.isPlaying()) {
            exoPlayer.stop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        exoPlayer.setPlayWhenReady(false);
        exoPlayer.getPlaybackState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.getPlaybackState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.getPlaybackState();
    }

    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


}