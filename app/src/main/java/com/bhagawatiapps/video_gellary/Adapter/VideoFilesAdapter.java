package com.bhagawatiapps.video_gellary.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bhagawatiapps.video_gellary.Activitys.VideoPlayerActivity;
import com.bhagawatiapps.video_gellary.Model.MediaFiles;
import com.bhagawatiapps.video_gellary.R;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.ArrayList;

public class VideoFilesAdapter extends RecyclerView.Adapter<VideoFilesAdapter.ViewHolder> {

    Context context;
    ArrayList<MediaFiles> VideoFiles;
    BottomSheetDialog bottomSheetDialog;

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

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.videoName.setText(VideoFiles.get(position).getDisplay_name());

        String size = VideoFiles.get(position).getSize();
        holder.videoSize.setText(android.text.format.Formatter.formatFileSize(context, Long.parseLong(size)));

        int duration;
        if (VideoFiles.get(position).getDuration() == null)
            duration = 0;
        else
            duration = Integer.parseInt(VideoFiles.get(position).getDuration());

        double milliSeconds = Double.parseDouble(String.valueOf(duration));

        holder.video_duration.setText(timeConversion((long) milliSeconds));

        Glide.with(context).load(VideoFiles.get(position).getPath()).into(holder.videoThumbnail);

        holder.video_menu_more.setOnClickListener(v -> {
            bottomSheetDialog = new BottomSheetDialog(context);

            View bsView = LayoutInflater.from(context).inflate(R.layout.video_bs_layout, v.findViewById(R.id.bottom_sheet));

            bottomSheetDialog.setContentView(bsView);
            bottomSheetDialog.show();

            bsView.findViewById(R.id.bs_play).setOnClickListener(v1 -> {
                holder.itemView.performClick();
                bottomSheetDialog.dismiss();
            });

            bsView.findViewById(R.id.bs_rename).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Rename Video");

                    View customLayout = LayoutInflater.from(context).inflate(R.layout.rename_layout, null);
                    builder.setView(customLayout);

                    EditText editText = customLayout.findViewById(R.id.rename_edit_text);
                    String filePath = VideoFiles.get(position).getPath();
                    final File file = new File(filePath);
                    Log.d("FilePath", filePath);
                    String CurrentName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."));
                    editText.setText(CurrentName);
                    Log.d("CurrentName", CurrentName);

                    builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newFileName = editText.getText().toString();
                            if (newFileName.isEmpty()) {
                                Toast.makeText(context, "File name cannot be empty", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String ext = file.getName().substring(file.getName().lastIndexOf("."));
                            String newFilePath = newFileName + ext;

                            // Convert file path to content URI
                            Uri contentUri = getContentUriFromFilePath(filePath);
                            if (contentUri == null) {
                                Toast.makeText(context, "File not found in MediaStore.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            ContentResolver resolver = context.getApplicationContext().getContentResolver();
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Video.Media.DISPLAY_NAME, newFilePath);

                            int rowUpdated = resolver.update(contentUri, values, null, null);
                            if (rowUpdated > 0) {
                                Toast.makeText(context, "Rename Successful", Toast.LENGTH_SHORT).show();
                                VideoFiles.get(position).setDisplay_name(newFileName);
                                notifyItemChanged(position);
                            } else {
                                Toast.makeText(context, "Rename Failed", Toast.LENGTH_SHORT).show();
                            }

                            bottomSheetDialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        });

        holder.itemView.setOnClickListener(v -> {
            if (position != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra("videoPath", VideoFiles.get(position).getPath());
                intent.putExtra("position", position);
                intent.putExtra("videoTitle", VideoFiles.get(position).getDisplay_name());
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("videoList", VideoFiles);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return VideoFiles.size();
    }

    public String timeConversion(long value) {
        String videoTime;
        int duration = (int) value;
        int hours = duration / 3600000;
        int minutes = (duration / 60000) % 60000;
        int seconds = duration % 60000 / 1000;
        if (hours > 0) {
            videoTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            videoTime = String.format("%02d:%02d", minutes, seconds);
        }
        return videoTime;
    }

    private Uri getContentUriFromFilePath(String filePath) {
        Uri contentUri = null;
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = { MediaStore.Video.Media._ID };
        String selection = MediaStore.Video.Media.DATA + "=?";
        String[] selectionArgs = new String[] { filePath };

        try (Cursor cursor = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        )) {
            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
            }
        }
        return contentUri;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView videoName, videoSize, video_duration;
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
}
