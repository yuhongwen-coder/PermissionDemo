package com.application.picture;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;


import com.application.Utils;
import com.application.picture.callback.Callback;

import java.io.File;

public class PhotoHelper implements ResultHandler.Listener {

    public static final int TYPE_TAKE_PHOTO = 10010;

    public static final int TYPE_CHOOSE_PHOTO = 10011;

    private FragmentActivity activity;

    private ResultHandler resultHandler;

    private Callback callback;

    private PhotoHelper(Context context) {
        if (context == null) {
            throw new RuntimeException("context must not be null");
        }
        activity = Utils.getActivity(context);
        if (activity == null) {
            throw new RuntimeException("context must not be CommonFragmentActivity");
        }
        resultHandler = new ResultHandler(this);

    }

    public static PhotoHelper with(Context context) {
        return new PhotoHelper(context);
    }


    /**
     * 删除缓存
     *
     * @param callback
     */
    public static void clearCache(Callback callback) {

    }

    /**
     * 删除所有缓存
     */
    public static void clearAllCache() {

    }


    private void startChoosePhotoIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        resultHandler.startActivityForResult(intent, TYPE_CHOOSE_PHOTO);


    }

    private void startTakePhotoIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File filesDir = activity.getExternalFilesDir("/");
        File parentFile = new File(filesDir, "/pictures");
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        File picFile = new File(parentFile, System.currentTimeMillis() + ".jpg");
        Uri uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider",picFile);
        callback.setUri(uri);
        callback.setFile(picFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        resultHandler.startActivityForResult(intent, TYPE_TAKE_PHOTO);
    }



    private boolean check(int type) {

        boolean permissionStorage = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (type == TYPE_CHOOSE_PHOTO) {
            return permissionStorage;
        } else if (type == TYPE_TAKE_PHOTO) {
            return ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && permissionStorage;
        }
        return false;

    }


    public void takePhoto(Callback callback) {
        start(TYPE_TAKE_PHOTO, callback);
    }


    public void choosePhoto(Callback callback) {
        start(TYPE_CHOOSE_PHOTO, callback);
    }


    private void start(int type, Callback callback) {
        if (callback == null) {
            return;
        }
        boolean check = check(type);
        if (!check) {
            callback.error("未获取到相关权限");
            return;
        }
        this.callback = callback;
        boolean success = addHandler();
        if (!success) {
            return;
        }
        if (type == TYPE_CHOOSE_PHOTO) {
            startChoosePhotoIntent();
        } else if (type == TYPE_TAKE_PHOTO) {
            startTakePhotoIntent();
        }
    }


    private boolean addHandler() {
        if (activity == null || callback == null) {
            return false;
        }
        resultHandler.setCallback(callback);
        FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
        supportFragmentManager.beginTransaction()
                .add(resultHandler, callback.getKey())
                .commitNowAllowingStateLoss();

        return true;

    }


    private void removeHandler() {
        if (activity == null || callback == null) {
            return;
        }
        FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
        supportFragmentManager.beginTransaction()
                .remove(resultHandler)
                .commitNow();
    }


    @Override
    public void resultEnd() {
        removeHandler();
    }
}
