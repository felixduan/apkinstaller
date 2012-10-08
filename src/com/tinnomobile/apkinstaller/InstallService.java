
package com.tinnomobile.apkinstaller;

import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class InstallService extends Service {

    private static final String DEFAULT = "default";
    private static final String FIRST_BOOT = "first_boot";
    private static final String TAG = "TinnoApkInstaller";
    private PackageInstallObserver obs;
    private PackageManager pkgManager;
    private SharedPreferences sp;
    private boolean bFirstBoot;

    @Override
    public IBinder onBind(Intent arg0) {
        log("onBind");
        return null;
    }

    @Override
    public void onCreate() {
        log("onCreate");
        super.onCreate();
        obs = new PackageInstallObserver();
        pkgManager = getPackageManager();
        sp = getSharedPreferences(DEFAULT, MODE_PRIVATE);
        bFirstBoot = sp.getBoolean(FIRST_BOOT, true);
    }

    @Override
    public void onDestroy() {
        log("onDestroy");
        super.onDestroy();
    }

    @Override
    @Deprecated
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        log("onStart bFirstBoot = " + bFirstBoot);
        if (bFirstBoot)
            installApk();
        else
            stopSelf();
    }

    /*
     * For now, please put legal apk files under /system/apksToInstall.
     * Do NOT support file checking.
     * Do NOT support subdir.
     */
    private void installApk() {
        File[] apkFiles = new File("/system/apksToInstall").listFiles();
        for (File apkFile : apkFiles) {
            if (apkFile.exists() && apkFile.isFile()) {
                log("installApk() apkFile = " + apkFile.getAbsolutePath());
                pkgManager.installPackage(Uri.fromFile(apkFile),
                        obs,
                        PackageManager.INSTALL_REPLACE_EXISTING,
                        null);
            } else {
                log("installApk() apkFile NOT exists OR is not a file: "
                        + apkFile.getAbsolutePath());
            }
        }
    }

    class PackageInstallObserver extends IPackageInstallObserver.Stub {
        boolean finished = false;
        int result;

        public void packageInstalled(String name, int status) {
            log("packageInstalled()   name = " + name + " status = " + status);
            sp.edit().putBoolean(FIRST_BOOT, false).commit();
            stopSelf();
        }
    }

    private void log(String s) {
        Log.d(TAG, s);
    }
}
