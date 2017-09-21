package com.btpn.sinaya.eform.utils;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.btpn.sinaya.eform.MTFBaseActivity;
import com.btpn.sinaya.eform.alert.listener.MTFConfirmationAlertDialogListener;

public class MTFPermissionUtil {

    public static void requestPermission(final MTFBaseActivity context, final String permission, final int permissionCode, String explanation){
        if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    permission)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                context.showConfirmationAlertDialog(explanation, new MTFConfirmationAlertDialogListener() {
                    @Override
                    public void onOK() {
                        ActivityCompat.requestPermissions(context,
                                new String[]{permission},
                                permissionCode);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(context,
                        new String[]{permission},
                        permissionCode);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

}
