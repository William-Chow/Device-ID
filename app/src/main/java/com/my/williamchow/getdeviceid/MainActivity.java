package com.my.williamchow.getdeviceid;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

// Device ID Main Class
public class MainActivity extends AppCompatActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private Button btnGrabDeviceID, btnSendEmail;
    private TextView tvResult;

    private String imei = "", wifiMAC = "", bindDeviceID = "";
    private String INTERCEPTOR = "-IMAC-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        listener();
    }

    private void init() {
        btnGrabDeviceID = (Button) findViewById(R.id.btnGrabDeviceID);
        tvResult = (TextView) findViewById(R.id.tvResult);
        btnSendEmail = (Button) findViewById(R.id.btnSendEmail);
    }

    private void listener() {
        btnGrabDeviceID.setOnClickListener(this);
        btnSendEmail.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGrabDeviceID:
//                if (Build.VERSION.SDK_INT >= 23) {
                    if (PermissionUtil.permissionCheck(MainActivity.this, PermissionUtil.REQUEST_TELEPHONE)) {
                        getI_mei();
                        tvResult.setText(bindDeviceID);
                    } else {
                        PermissionUtil.requestPermission(this, PermissionUtil.REQUEST_TELEPHONE);
                    }
//                } else {
//                    getI_mei();
//                    tvResult.setText(bindDeviceID);
//                }
                break;
            case R.id.btnSendEmail:
                if (isInternetOn()) {
                    if (bindDeviceID.length() != 0) {
                        googleIntent();
                    }
                } else {
                    Toast.makeText(this, "No Internet, failed to send email", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private String getSerial() {
        return android.os.Build.VERSION.SDK_INT >= 9 ? android.os.Build.SERIAL : "";
    }

    public String getWifiMACAddress() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        return wifiManager.getConnectionInfo().getMacAddress();
    }

    public String getWifiMACAddressWithoutSemicolon() {
        String wifiMAC = getWifiMACAddress();
        wifiMAC = getWifiMACAddress() == null ? "" : getWifiMACAddress().replaceAll(":", "");
        return wifiMAC;
    }

    private void getI_mei() {
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        if (tm.getDeviceId() != null) {
            imei = tm.getDeviceId();
        } else {
            imei = "";
        }

        if (getSerial().equals("unknown")) {
            wifiMAC = getWifiMACAddressWithoutSemicolon();
            bindDeviceID = imei + INTERCEPTOR + wifiMAC;
        } else {
            if (imei == null) {
                if (getSerial().length() > 0) {
                    wifiMAC = getWifiMACAddressWithoutSemicolon();
                    bindDeviceID = getSerial() + INTERCEPTOR + wifiMAC;
                } else {
                    wifiMAC = getWifiMACAddressWithoutSemicolon();
                    bindDeviceID = imei + INTERCEPTOR + wifiMAC;
                }
            } else
                bindDeviceID = getSerial() + INTERCEPTOR + imei;
        }
    }

    // William Chow check internet connection
    private Boolean isInternetOn() {
        // get Connectivity Manager object to check connection
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                Toast.makeText(this, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                Toast.makeText(this, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
            }
            return true;
        } else {
            // not connected to the internet
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void googleIntent() {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setType("plain/text");
//        sendIntent.setData(Uri.parse("williamchow@silverglobe.com"));
        sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
//        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"williamchow@silverglobe.com"});
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Grab Device ID");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "" + bindDeviceID);
        startActivity(sendIntent);
    }

    @Override
    public void onBackPressed() {
        PromptAlertDialog.alertDialogExit(MainActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtil.REQUEST_TELEPHONE) {
            if (!PermissionUtil.verifyPermissions(grantResults)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                    PromptAlertDialog.promptTelephonyReasonDialog(MainActivity.this, getResources().getString(R.string.permission_telephony_Reason), PermissionUtil.REQUEST_TELEPHONE, getResources().getString(R.string.permission_telephony_title));
                } else {
                    PromptAlertDialog.promptTelephonyReasonDialog_GotoPermission(MainActivity.this, getResources().getString(R.string.permission_telephony_Reason_Settings), getResources().getString(R.string.permission_telephony_title));
                }
            } else {
                getI_mei();
                tvResult.setText(bindDeviceID);
            }
        }
    }
}
