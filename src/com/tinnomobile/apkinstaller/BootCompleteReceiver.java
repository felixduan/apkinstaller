
package com.tinnomobile.apkinstaller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {
    private static final String TAG = "TinnoApkInstaller";

    @Override
    public void onReceive(Context context, Intent bootintent) {
        log("onReceive");
        Intent mServiceIntent = new Intent();
        mServiceIntent.setAction("com.tinnomobile.apkinstaller.InstallService");
        context.startService(mServiceIntent);
        log("onReceive done");
    }

    private void log(String s) {
        Log.d(TAG, s);
    }
}
