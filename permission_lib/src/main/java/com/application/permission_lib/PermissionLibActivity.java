package com.application.permission_lib;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.application.picture.PhotoHelper;
import com.application.picture.callback.FileCallback;
import com.bumptech.glide.Glide;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;

public class PermissionLibActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PermissionLibActivity";
    private ImageView uploadPic;
    private String picPath;
    private boolean isUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_lib);
        uploadPic = findViewById(R.id.upload_pic);
        uploadPic.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.upload_pic) {
            AndPermission.with(this)
                    .runtime()
                    .permission(Permission.WRITE_EXTERNAL_STORAGE,Permission.CAMERA)
                    .onGranted(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            Log.e(TAG,"onGranted 权限申请了");
                            for (int i = 0;i<data.size();i++) {
                                Log.e(TAG,"onGranted 申请的权限是 = " + data.get(i));
                                configPhotoChoseDialog(data.get(i));
                            }
                        }
                    })
                    .onDenied(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            Log.e(TAG,"onDenied 权限被用户拒绝了");
                        }
                    })
                    .start();
        }
    }

    private void configPhotoChoseDialog(String permission) {
        if (permission.contains(Permission.CAMERA)) {
            PhotoChooseDialog photoChooseDialog = new PhotoChooseDialog(this);
            photoChooseDialog.setListener(new PhotoChooseDialog.Listener() {
                @Override
                public void choosePhoto(final Dialog dialog) {
                    PhotoHelper.with(PermissionLibActivity.this).choosePhoto(new FileCallback() {
                        @Override
                        public void result(String filePath) {
                            picPath = filePath;
                            isUpload = false;
                            Glide.with(PermissionLibActivity.this).load(filePath).into(uploadPic);
                            dialog.dismiss();
                        }

                        @Override
                        public void error(String msg) {
                            dialog.dismiss();

                        }

                        @Override
                        public void cancel() {

                        }
                    });

                }

                @Override
                public void takePhoto(final Dialog dialog) {
                    PhotoHelper.with(PermissionLibActivity.this).takePhoto(new FileCallback() {
                        @Override
                        public void result(String filePath) {
                            picPath = filePath;
                            isUpload = false;
                            Glide.with(PermissionLibActivity.this).load(filePath).into(uploadPic);
                            dialog.dismiss();

                        }

                        @Override
                        public void error(String msg) {
                            dialog.dismiss();

                        }

                        @Override
                        public void cancel() {

                        }
                    });
                }
            });
            photoChooseDialog.show();
        }
    }
}
