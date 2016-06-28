package com.reverie.sdktutorial;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.sdktest.R;

/**
 * Created by ranjan_mac on 23/05/16.
 */
public class SdkTestLaunchActivity extends Activity {

    private static final int READ_PHONE_STATE_REQUEST_CODE = 101;
    private TextView lmLinkTV, downloadManagerLinkTV, keypadLinkTV, uiLinkTV, transLocalLinkTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sdk_test_launch);

        lmLinkTV = (TextView) findViewById(R.id.lmLinkTV);
        downloadManagerLinkTV = (TextView) findViewById(R.id.downloadManagerLinkTV);
        keypadLinkTV = (TextView) findViewById(R.id.keypadLinkTV);
        uiLinkTV = (TextView) findViewById(R.id.uiLinkTV);
        transLocalLinkTV = (TextView) findViewById(R.id.transLocalLinkTV);


        int phoneStatePermission = ContextCompat.checkSelfPermission(SdkTestLaunchActivity.this, Manifest.permission.READ_PHONE_STATE);
        if(phoneStatePermission == PackageManager.PERMISSION_GRANTED) {
            // launch app menu
        }
        else {
            ActivityCompat.requestPermissions(SdkTestLaunchActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE},READ_PHONE_STATE_REQUEST_CODE);
        }


        /*
        lmLinkTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SdkTestLaunchActivity.this, SdkTestLM.class);
                startActivity(intent);
            }
        });

        downloadManagerLinkTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SdkTestLaunchActivity.this, SdkTestDownloadResource.class);
                startActivity(intent);
            }
        });
        */

        keypadLinkTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SdkTestLaunchActivity.this, SdkTestKeypad.class);
                startActivity(intent);
            }
        });


        uiLinkTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SdkTestLaunchActivity.this, SdkTestUIComponents.class);
                startActivity(intent);
            }
        });


        transLocalLinkTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SdkTestLaunchActivity.this, SdkTestLocalTrans.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_PHONE_STATE_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                } else {
                    Toast.makeText(SdkTestLaunchActivity.this, "Permission Denied, Exit..", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }
}
