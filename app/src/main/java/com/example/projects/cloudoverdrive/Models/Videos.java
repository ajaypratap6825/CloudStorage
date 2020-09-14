package com.example.projects.cloudoverdrive.Models;

import com.google.firebase.database.Exclude;

public class Videos {
    private String mUrl;
    private String mKey;
    String filename;

    public Videos() {
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Videos(String mUrl, String filename) {
        this.mUrl = mUrl;
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

