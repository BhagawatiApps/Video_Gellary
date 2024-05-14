package com.bhagawatiapps.video_gellary.Model;

public class MediaFiles {

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


}
