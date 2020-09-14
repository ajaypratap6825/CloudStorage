package com.example.projects.cloudoverdrive.Models;

import com.google.firebase.database.Exclude;

public class Images {
    public  String url;
    private String mKey;
    String filename;

    public Images() {
    }

    public Images(String url,  String filename) {
        this.url = url;
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Images(String mUrl) {
        this.url = mUrl;

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
