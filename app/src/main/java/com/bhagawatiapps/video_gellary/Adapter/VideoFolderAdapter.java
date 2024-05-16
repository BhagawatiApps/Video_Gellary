package com.bhagawatiapps.video_gellary.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bhagawatiapps.video_gellary.Activitys.VideoActivity;
import com.bhagawatiapps.video_gellary.Model.MediaFiles;
import com.bhagawatiapps.video_gellary.R;

import java.util.ArrayList;

public class VideoFolderAdapter extends RecyclerView.Adapter<VideoFolderAdapter.VideoFolderViewHolder> {


    Context context;
    ArrayList<MediaFiles> mediaFiles;
    ArrayList<String> FolderList = new ArrayList<>();

    public VideoFolderAdapter(Context context, ArrayList<MediaFiles> mediaFiles, ArrayList<String> FolderList) {
        this.context = context;
        this.mediaFiles = mediaFiles;
        this.FolderList = FolderList;
    }

    @NonNull
    @Override
    public VideoFolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_folder_item, parent, false);

        return new VideoFolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoFolderViewHolder holder, int position) {

        int index = FolderList.get(position).lastIndexOf("/");
        String folderName = FolderList.get(position).substring(index + 1);
        holder.folderName.setText(folderName);
        holder.folderPath.setText(FolderList.get(position));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VideoActivity.class);
            intent.putExtra("folderName", folderName);
            context.startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return FolderList.size();
    }

    public class VideoFolderViewHolder extends RecyclerView.ViewHolder {

        TextView folderName, videoCount, folderPath;

        public VideoFolderViewHolder(@NonNull View itemView) {
            super(itemView);

            folderName = itemView.findViewById(R.id.folderName);
            videoCount = itemView.findViewById(R.id.folderCount);
            folderPath = itemView.findViewById(R.id.folderPath);
        }

    }


}
