package com.example.projects.cloudoverdrive.Models;

import com.google.firebase.database.Exclude;

public class Audios {
    private String mUrl;
    private String mKey;
    String filename;

    public Audios() {
    }

    public Audios(String mUrl,  String filename) {
        this.mUrl = mUrl;
        this.filename = filename;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }
    @Exclude
    public void setKey(String key) {
        mKey = key;
    }
}
