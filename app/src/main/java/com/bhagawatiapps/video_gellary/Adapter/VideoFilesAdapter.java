package com.bhagawatiapps.video_gellary.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bhagawatiapps.video_gellary.Activitys.VideoPlayerActivity;
import com.bhagawatiapps.video_gellary.Model.MediaFiles;
import com.bhagawatiapps.video_gellary.R;

import java.util.ArrayList;

public class VideoFilesAdapter extends RecyclerView.Adapter<VideoFilesAdapter.ViewHolder> {

    Context context;
    ArrayList<MediaFiles> VideoFiles;

    public VideoFilesAdapter(Context context, ArrayList<MediaFiles> videoFiles) {
        this.context = context;
        VideoFiles = videoFiles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.videoName.setText(VideoFiles.get(position).getDisplay_name());

        String size = VideoFiles.get(position).getSize();
        holder.videoSize.setText(android.text.format.Formatter.formatFileSize(context,Long.parseLong(size)));

        double milliSeconds = Double.parseDouble(VideoFiles.get(position).getDuration());

        holder.video_duration.setText(timeConversion((long) milliSeconds));

        

        holder.video_menu_more.setOnClickListener(v -> {
            Toast.makeText(context, "Menu", Toast.LENGTH_SHORT).show();
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VideoPlayerActivity.class);
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return VideoFiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView videoName, videoSize,video_duration;
        ImageView videoThumbnail, video_menu_more;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoName = itemView.findViewById(R.id.video_title);
            videoSize = itemView.findViewById(R.id.video_size);
            video_duration = itemView.findViewById(R.id.video_duration);
            videoThumbnail = itemView.findViewById(R.id.Tumbnail);
            video_menu_more = itemView.findViewById(R.id.video_menu_more);
        }
    }

    public String timeConversion(long value){
        String videoTime;
        int duration = (int) value;
        int hours = duration / 3600000;
        int minutes = (duration / 60000) %60000;
        int seconds = duration%60000/1000;
        if (hours>0){
            videoTime = String.format("%02d:%02d:%02d",hours,minutes,seconds);
        }
        else
            videoTime = String.format("%02d:%02d",minutes,seconds);

        return videoTime;
    }

}
