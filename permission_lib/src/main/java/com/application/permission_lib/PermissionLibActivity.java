package com.application.permission_lib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class PermissionLibActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView uploadPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_lib);
        uploadPic = findViewById(R.id.upload_pic);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.upload_pic) {

        }
    }
}
