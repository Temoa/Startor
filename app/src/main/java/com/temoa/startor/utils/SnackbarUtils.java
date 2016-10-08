package com.temoa.startor.utils;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Temoa
 * on 2016/9/6 13:51
 */
public class SnackbarUtils {

    private SnackbarUtils() {

    }

    public static void show(View v, String content) {
        Snackbar snackbar = Snackbar.make(v, content, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(Color.parseColor("#212121"));
        snackbar.show();
    }

    public static void showWithAction(View v, String content, String action, View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(v, content, Snackbar.LENGTH_INDEFINITE)
                .setAction(action, listener)
                .setActionTextColor(Color.parseColor("#ffffff"));
        snackbar.getView().setBackgroundColor(Color.parseColor("#212121"));
        snackbar.show();

    }
}
