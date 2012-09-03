
package com.tinnomobile.apkinstaller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ApkInstallerTest extends Activity implements OnClickListener {
    private static final String TAG = "ApkInstallerTest";
    private Button mButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.button1);
        mButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        log("onClick()");
        if (v.getId() == R.id.button1) {
            // installDemoApk();
            Intent mServiceIntent = new Intent();
            mServiceIntent.setAction("com.tinnomobile.apkinstaller.InstallService");
            startService(mServiceIntent);
        }
    }

    private void log(String s) {
        Log.d(TAG, s);
    }
}
