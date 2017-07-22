package com.exmple.xiaoguy.demo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityInfo activityInfo;
        String pkgName = getPackageName();
        String clsName = getClass().getCanonicalName();
        try {
            activityInfo = getPackageManager().getActivityInfo(
                    new ComponentName(pkgName, clsName), PackageManager.MATCH_DEFAULT_ONLY);
        }
        catch (NameNotFoundException e) {
            e.printStackTrace();
            Log.e("size", "get activity info failed: " + getPackageName() + getClass().getCanonicalName());
            return;
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_soft_overlay:
                startActivity(new Intent(this, SoftInputOverlayActivity.class));
                break;
        }
    }
}
