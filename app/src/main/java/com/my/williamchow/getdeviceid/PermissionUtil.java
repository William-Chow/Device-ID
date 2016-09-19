package com.my.williamchow.getdeviceid;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by William Chow on 9/2/2016.
 */
public class PermissionUtil {

    public static final int REQUEST_TELEPHONE = 1;
    public static String[] PERMISSIONS_TELEPHONE = {Manifest.permission.READ_PHONE_STATE};

    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean permissionCheck(Activity activity, int permissionIndex) {
        if (permissionIndex == REQUEST_TELEPHONE) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        }

        return false;
    }

    public static void requestPermission(Activity activity, int permissionIndex) {
        if (permissionIndex == REQUEST_TELEPHONE) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_TELEPHONE, REQUEST_TELEPHONE);
        }
    }
}
