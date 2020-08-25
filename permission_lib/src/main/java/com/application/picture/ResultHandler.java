package com.application.picture;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.application.picture.callback.Callback;
import com.application.picture.callback.FileCallback;
import com.application.picture.callback.UriCallback;


/**
 * 处理回调
 */
@SuppressLint("ValidFragment")
public class ResultHandler extends Fragment {


    private Callback callback;


    private Listener listener;

    public interface Listener {

        void resultEnd();
    }


    public ResultHandler(Listener listener) {
        if (listener == null) {
            throw new RuntimeException("Listener must not be null");
        }
        this.listener = listener;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }


    @Override
    public void onAttach( Context context) {
        super.onAttach(context);
        Log.i("xiaoqiao", "onAttach");
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("xiaoqiao", "onDetach");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callback == null) {
            return;
        }

        if (resultCode != Activity.RESULT_OK) {
            callback.cancel();
            listener.resultEnd();
            return;
        }

        if (requestCode == PhotoHelper.TYPE_CHOOSE_PHOTO) {
            //选择图片
            if (data == null) {
                callback.error("Intent data is null");
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                handleImageOnKitKat(data);
            } else {
                handleImageBeforeKitKat(data);
            }

        } else if (requestCode == PhotoHelper.TYPE_TAKE_PHOTO) {
            //拍照
            if (callback instanceof FileCallback) {
                FileCallback fileCallback = (FileCallback) callback;
                if (callback.getFile() != null) {
                    fileCallback.result(callback.getFile().getAbsolutePath());
                }

            } else if (callback instanceof UriCallback) {
                UriCallback uriCallback = (UriCallback) this.callback;
                uriCallback.result(callback.getUri());
            }
        }

        listener.resultEnd();

    }


    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        Uri uri = data.getData();
        if (uri == null) {
            return;
        }

        if (callback instanceof UriCallback) {
            UriCallback uriCallback = (UriCallback) callback;
            uriCallback.result(uri);
        } else if (callback instanceof FileCallback) {
            FileCallback fileCallback = (FileCallback) callback;
            String imagePath = null;

            if (DocumentsContract.isDocumentUri(getContext(), uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content: //downloads/public_downloads"), Long.valueOf(docId));
                    imagePath = getImagePath(contentUri, null);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                imagePath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                imagePath = uri.getPath();
            }
            fileCallback.result(imagePath);
        }


    }


    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContext().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


}
