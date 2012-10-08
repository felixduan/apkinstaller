
package com.tinnomobile.apkinstaller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {
    private static final String TAG = "TinnoApkInstaller";
    private static final String SDCARD_1 = "file:///mnt/sdcard";

    @Override
    public void onReceive(Context context, Intent bootintent) {
        log("onReceive");
        if (bootintent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent mServiceIntent = new Intent();
            mServiceIntent.setAction("com.tinnomobile.apkinstaller.InstallService");
            mServiceIntent.putExtra(TAG, InstallService.BOOTED);
            context.startService(mServiceIntent);
        } else if (bootintent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)) {
            Intent mServiceIntent = new Intent();
            mServiceIntent.setAction("com.tinnomobile.apkinstaller.InstallService");
            mServiceIntent.putExtra(TAG, InstallService.MOUNTED);
            context.startService(mServiceIntent);
        }
        log("onReceive done");
    }

    private void log(String s) {
        Log.d(TAG, s);
    }
}
