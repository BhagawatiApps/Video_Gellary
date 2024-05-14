package com.bhagawatiapps.video_gellary.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bhagawatiapps.video_gellary.Model.MediaFiles;
import com.bhagawatiapps.video_gellary.R;

import java.util.ArrayList;

public class VideoFolderAdapter extends RecyclerView.Adapter<VideoFolderAdapter.VideoFolderViewHolder> {


    Context context;
    ArrayList<MediaFiles> mediaFiles;
    ArrayList<String> FolderList;

    public VideoFolderAdapter(Context context, ArrayList<MediaFiles> mediaFiles, ArrayList<String> folderList) {
        this.context = context;
        this.mediaFiles = mediaFiles;
        FolderList = folderList;
    }

    @NonNull
    @Override
    public VideoFolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_folder_item,parent,false);

        return new VideoFolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoFolderViewHolder holder, int position) {

        //error 1 : index out of bound error

        int index = FolderList.get(position).lastIndexOf("/");
        String Folder_Name = FolderList.get(position).substring(index+1);
        holder.folderName.setText(Folder_Name);



    }

    @Override
    public int getItemCount() {
        return mediaFiles.size();
    }

    public class VideoFolderViewHolder extends RecyclerView.ViewHolder {

        TextView folderName, videoCount,folderPath;

        public VideoFolderViewHolder(@NonNull View itemView) {
            super(itemView);

            folderName = itemView.findViewById(R.id.folderName);
            videoCount = itemView.findViewById(R.id.folderCount);
            folderPath = itemView.findViewById(R.id.folderPath);
        }

    }
}
