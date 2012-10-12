
package com.tinnomobile.apkinstaller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

public class InstallService extends Service {

    public static final String DEFAULT = "default";
    public static final String FIRST_INSTALL = "first_install"; // install apks at the first time boot
    public static final String FIRST_COPY = "first_copy"; // copy file at the first time mount
    public static final String TAG = "TinnoApkInstaller";
    public static final int BOOTED = 0;
    public static final int MOUNTED = 1;
    private PackageInstallObserver obs;
    private PackageManager pkgManager;
    private SharedPreferences sp;

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
        int data = intent.getIntExtra(TAG, 0);
        log("onStart data = " + data);
        switch (data) {
            case BOOTED:
                installApk();
                break;
            case MOUNTED:
                copyFile();
                break;
            default:
                stopSelf();
        }
    }

    /*
     * For now, please put legal apk files under /system/apksToInstall. Do NOT
     * support file checking. Do NOT support subdir.
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
        sp.edit().putBoolean(FIRST_INSTALL, false).commit();
    }

    /*
     * copy file from /system/apksToInstall/filesToCopy/ to a certain
     * destination unless already exists.
     */
    private void copyFile() {
        log("copyFile");
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            log("External SD card not mounted");
            return;
        }
        File source = new File("/system/apksToInstall/filesToCopy/EN-BN_enwiktionary.quickdic");
        File dir = new File(Environment.getExternalStorageDirectory()
                .getPath() + "/quickDic");
        if (!dir.exists()) dir.mkdir();
        File dest = new File(Environment.getExternalStorageDirectory()
                .getPath() + "/quickDic/EN-BN_enwiktionary.quickdic");

        if (source.exists() && !dest.exists()) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new FileInputStream(source.getAbsolutePath());
                out = new FileOutputStream(dest.getAbsolutePath());
                int length = in.available();
                int len = (length % 1024 == 0) ? (length / 1024)
                        : (length / 1024 + 1);
                byte[] temp = new byte[1024];
                for (int i = 0; i < len; i++) {
                    in.read(temp);
                    out.write(temp);
                }
                log("copy done");
                sp.edit().putBoolean(FIRST_COPY, false).commit();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        log("IOException 3333");
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        log("IOException 4444");
                        e.printStackTrace();
                    }
                }
            }

        } else {
            log("file NOT exists : " + source.getAbsolutePath());
        }

    }

    class PackageInstallObserver extends IPackageInstallObserver.Stub {
        boolean finished = false;
        int result;

        public void packageInstalled(String name, int status) {
            log("packageInstalled()   name = " + name + " status = " + status);
            //sp.edit().putBoolean(FIRST_BOOT, false).commit();
        }
    }

    private void log(String s) {
        Log.d(TAG, s);
    }
}
