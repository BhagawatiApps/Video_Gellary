package com.bhagawatiapps.video_gellary.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class MediaFiles implements Parcelable {

    String Id;
    String Title;
    String Display_name;
    String Size;
    String Duration;
    String Path;
    String Added_date;

    public MediaFiles(String id, String title, String display_name, String size, String duration, String path, String added_date) {
        Id = id;
        Title = title;
        Display_name = display_name;
        Size = size;
        Duration = duration;
        Path = path;
        Added_date = added_date;
    }


    protected MediaFiles(Parcel in) {
        Id = in.readString();
        Title = in.readString();
        Display_name = in.readString();
        Size = in.readString();
        Duration = in.readString();
        Path = in.readString();
        Added_date = in.readString();
    }

    public static final Creator<MediaFiles> CREATOR = new Creator<MediaFiles>() {
        @Override
        public MediaFiles createFromParcel(Parcel in) {
            return new MediaFiles(in);
        }

        @Override
        public MediaFiles[] newArray(int size) {
            return new MediaFiles[size];
        }
    };

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDisplay_name() {
        return Display_name;
    }

    public void setDisplay_name(String display_name) {
        Display_name = display_name;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getAdded_date() {
        return Added_date;
    }

    public void setAdded_date(String added_date) {
        Added_date = added_date;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(Title);
        dest.writeString(Display_name);
        dest.writeString(Size);
        dest.writeString(Duration);
        dest.writeString(Path);
        dest.writeString(Added_date);
    }
}
