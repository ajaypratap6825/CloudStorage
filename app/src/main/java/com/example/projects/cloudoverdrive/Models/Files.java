package com.example.projects.cloudoverdrive.Models;

import com.google.firebase.database.Exclude;

public class Files {
    private String mUrl;
    private String mKey;
    String filename;

    public Files() {
    }

    public Files(String mUrl,  String filename) {
        this.mUrl = mUrl;
        this.filename = filename;

    }


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
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
