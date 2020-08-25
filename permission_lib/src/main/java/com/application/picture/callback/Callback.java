package com.application.picture.callback;

import android.net.Uri;

import java.io.File;

public abstract class Callback {


    private String key;

    private Uri uri;

    private File file;

    public Callback() {
        key = String.valueOf(System.currentTimeMillis());
    }

    public String getKey() {
        return key;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public abstract void error(String msg);

    public abstract void cancel();

    public void setFile(File picFile) {
        this.file = picFile;
    }

    public File getFile() {
        return file;
    }
}
