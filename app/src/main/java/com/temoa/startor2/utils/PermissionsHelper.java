package com.temoa.startor2.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.temoa.startor2.R;

/**
 * Created by Temoa
 * on 2016/10/28 13:30
 */

public class PermissionsHelper {

    public static boolean checkPermissions(Activity context, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_DENIED;
        }
        return true;
    }

    /**
     * 第一次请求权限时，用户拒绝了，调用shouldShowRequestPermissionRationale()后返回true
     * 用户在第一次拒绝某个权限后，下次再次申请时，授权的dialog中将会出现“不再提醒”选项，一旦选中勾选了，那么下次申请将不会提示用户
     * 第二次请求权限时，用户拒绝了，并选择了“不再提醒”的选项，调用shouldShowRequestPermissionRationale()后返回false
     * 在设置应用程序中,禁止了当前应用的权限, shouldShowRequestPermissionRationale()返回false
     *
     * @param context     上下文
     * @param permissions 请求的权限
     * @param requestCode 请求码
     */
    public static void requestPermissions(final Activity context, final String[] permissions, final int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(context, permissions[0]) == PackageManager.PERMISSION_DENIED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(context, permissions[0])) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(R.string.remind)
                            .setMessage(R.string.permission_desc).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(context, permissions, requestCode);
                                }
                            });
                    builder.create().show();
                } else {
                    // 未请求过权限时, shouldShowRequestPermissionRationale()返回false, 可以在第一次是请求权限
                    ActivityCompat.requestPermissions(context, permissions, requestCode);
                }
            }
        }
    }
}
