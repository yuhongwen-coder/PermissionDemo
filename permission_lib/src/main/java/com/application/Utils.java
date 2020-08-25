package com.application;

import android.content.Context;
import android.content.ContextWrapper;

import androidx.fragment.app.FragmentActivity;

public class Utils {
    public static FragmentActivity getActivity(Context context) {
        if (context instanceof FragmentActivity) {
            return (FragmentActivity) context;
        } else if (context instanceof ContextWrapper) {
            return getActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;

    }
}
