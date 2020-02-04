package com.omronble.bp_broadcast.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzx on 2018/3/8.
 */

public final class CheckPermissionUtils {
    public static final int REQUEST_MUST_PERM = 101;
    public static final int REQUEST_LOCATION_PERM = 102;
    public static final int REQUEST_CAMERA_PERM = 103;
    public static final int REQUEST_INSTALL_PACKAGES = 104;
    private Context mContext;
    public CheckPermissionUtils(Context context) {
        mContext = context.getApplicationContext();
    }

    // 判断权限集合
    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    // 判断是否缺少权限
    public boolean lacksPermission(String permission) {
        return !isPermission(permission);
    }

    //判断该permission是否已获取
    public boolean isPermission(String permission) {
        boolean isPermission;
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(mContext,
                    permission) != PackageManager.PERMISSION_GRANTED) {
                //先判断有没有权限 ，没有就在这里进行权限的申请
                isPermission = false;
            } else {
                //说明已经获取到摄像头权限了 想干嘛干嘛
                isPermission = true;
            }
        } else {//这个说明系统版本在6.0之下，不需要动态获取权限。
            isPermission = true;
        }
        return isPermission;
    }

    /**
     * 当前摄像头是否可用
     */
    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            mCamera.setDisplayOrientation(90);
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse) {
            mCamera.release();
            mCamera = null;
        }
        return canUse;
    }


    /**
     * 跳转到权限设置界面
     */
    public void getAppDetailSettingIntent(Activity activity, Context context, int requestCode){
        Intent intent = new Intent();
        if(Build.VERSION.SDK_INT >= 9){
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if(Build.VERSION.SDK_INT <= 8){
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        activity.startActivityForResult(intent, requestCode);
    }

    //APP需要申请的全部权限
    private String[] allPermissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE
    };
    //APP启动必须需要申请的权限
    private String[] mustPermissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    //GPS定位权限
    private String[] locationPermissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    //CAMERA定位权限
    private String[] cameraPermission = new String[]{
            Manifest.permission.CAMERA
    };

    //检测权限
    private String[] checkPermission(Context context, String[] permissions){
        List<String> data = new ArrayList<>();//存储未申请的权限
        for (String permission : permissions) {
            int checkSelfPermission = ContextCompat.checkSelfPermission(context, permission);
            if(checkSelfPermission == PackageManager.PERMISSION_DENIED){//未申请
                data.add(permission);
            }
        }
        return data.toArray(new String[data.size()]);
    }

    //检查本app需要主动申请的全部权限
    public String[] checkAllPermissions(Context context){
       return checkPermission(context, allPermissions);
    }

    //检查本app运行必备权限
    public String[] checkMustPermissions(Context context){
        return checkPermission(context, mustPermissions);
    }

    //是否有GPS权限
    public boolean hasLocationPermission(Context context) {
        return checkPermission(context, locationPermissions).length == 0;
    }

    //是否有CAMERA权限
    public boolean hasCameraPermission(Context context) {
        return checkPermission(context, cameraPermission).length == 0;
    }


}
