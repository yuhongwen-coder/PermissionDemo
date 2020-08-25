package com.application.permission_lib;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

//import androidx.annotation.NonNull;

public class PhotoChooseDialog extends Dialog {

    /**
     * 增加构造函数
     * @param context 构造函数的上下文
     */
    public PhotoChooseDialog(Context context) {
        super(context);
    }

    /**
     * 重写 onCreate() 函数，onCreate 函数是 View 显示页面的 基本函数，不管是 Dialog 还是 Activity 或者
     *  Fragment 都可以重写这个函数，并且 在这个函数中，加载布局文件  xml
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permisson_lib_dialog);
        configDialogParams();
        initListener();
    }

    private void initListener() {
        findViewById(R.id.tvTakePhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.takePhoto(PhotoChooseDialog.this);
                }
            }
        });

        findViewById(R.id.tvChoosePhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.choosePhoto(PhotoChooseDialog.this);
                }
            }
        });
        findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoChooseDialog.this.dismiss();
            }
        });
    }

    /**
     * 配置 Dialog 的参数
     */
    private void configDialogParams() {
        // 1 获取窗口对象
        Window window = getWindow();
        // 2-1 获取所有 View 的 根布局并设置相关的属性，因为所有 页面的 根布局都是 DecorView
        // 2-2 而 DecorView 作为View 的顶级 View， 后面的View 都会 add 在她上面
        // 2-3 而 Window 是什么了？ window 就是 容纳 View 的一个容器
        window.getDecorView().setPadding(0,0,0,0);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);
        window.getDecorView().setBackgroundResource(android.R.color.transparent);
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void choosePhoto(Dialog dialog);

        void takePhoto(Dialog dialog);
    }
}
