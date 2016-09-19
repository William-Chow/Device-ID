package com.my.williamchow.getdeviceid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by William Chow on 9/2/2016.
 */
public class PromptAlertDialog {

    // Exit App
    public static void alertDialogExit(Activity _activity) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(_activity);
        alertDialog.setTitle(_activity.getResources().getString(R.string.notice)).setMessage(_activity.getResources().getString(R.string.exit_app))
                .setNegativeButton(_activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(_activity.getResources().getString(R.string.exit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                }).setCancelable(false).show();
    }

    public static void promptTelephonyReasonDialog(final Activity activity, String permissionReason, final int permissionIndex, String permissionTitle) {
        Log.i("NIC", "Show rationale");
        AlertDialog.Builder popupBuilder = new AlertDialog.Builder(activity);
        popupBuilder.setTitle(permissionTitle);
        popupBuilder.setMessage(permissionReason);
        popupBuilder.setPositiveButton(R.string.permission_Allow, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                PermissionUtil.requestPermission(activity, permissionIndex);
                dialog.dismiss();
            }
        });
        popupBuilder.setNegativeButton(R.string.permission_NotNow, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                activity.finish();
            }
        });
        AlertDialog dialog = popupBuilder.show();
    }

    public static void promptTelephonyReasonDialog_GotoPermission(final Activity activity, String permissionReason, String permissionTitle) {
        Log.i("NIC", "Permission blocked");
        AlertDialog.Builder popupBuilder = new AlertDialog.Builder(activity);
        popupBuilder.setTitle(permissionTitle);
        popupBuilder.setMessage(permissionReason);
        popupBuilder.setPositiveButton(R.string.permission_Settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", activity.getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                dialog.dismiss();
            }
        });
        popupBuilder.setNegativeButton(R.string.permission_NotNow, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                activity.finish();
            }
        });
        AlertDialog dialog = popupBuilder.show();
    }
}
