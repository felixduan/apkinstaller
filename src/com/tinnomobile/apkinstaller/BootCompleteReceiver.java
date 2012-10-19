
package com.tinnomobile.apkinstaller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {
    private static final String SDCARD_1 = "file:///mnt/sdcard";
    private SharedPreferences sp;
    private boolean bFirstInstall;
    private boolean bFirstCopy;

    @Override
    public void onReceive(Context context, Intent bootintent) {
        log("onReceive");
        sp = context.getSharedPreferences(InstallService.DEFAULT, context.MODE_PRIVATE);
        bFirstInstall = sp.getBoolean(InstallService.FIRST_INSTALL, true);
        bFirstCopy = sp.getBoolean(InstallService.FIRST_COPY, true);
        if (bFirstInstall && bootintent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent mServiceIntent = new Intent();
            mServiceIntent.setAction("com.tinnomobile.apkinstaller.InstallService");
            mServiceIntent.putExtra(InstallService.TAG, InstallService.BOOTED);
            context.startService(mServiceIntent);
        }
        /*
         * else if (bFirstCopy &&
         * bootintent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)) { Intent
         * mServiceIntent = new Intent();
         * mServiceIntent.setAction("com.tinnomobile.apkinstaller.InstallService"
         * ); mServiceIntent.putExtra(InstallService.TAG,
         * InstallService.MOUNTED); context.startService(mServiceIntent); }
         */
        log("onReceive done");
    }

    private void log(String s) {
        Log.d(InstallService.TAG, s);
    }
}
