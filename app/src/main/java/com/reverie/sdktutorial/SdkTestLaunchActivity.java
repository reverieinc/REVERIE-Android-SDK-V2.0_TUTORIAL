package com.reverie.sdktutorial;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.reverie.manager.RevSDK;
import com.reverie.manager.ValidationCompleteListener;
import com.sdktest.R;

/**
 * Created by ranjan_mac on 23/05/16.
 */
public class SdkTestLaunchActivity extends Activity {

    private static final int READ_PHONE_STATE_REQUEST_CODE = 101;
    private TextView keypadLinkTV, uiLinkTV;
    private RelativeLayout keypadStatusRL;
    private TextView statusKeypadLmTV;
    private Button statusKeypadLmButton;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sdk_test_launch);

        keypadLinkTV = (TextView) findViewById(R.id.keypadLinkTV);
        uiLinkTV = (TextView) findViewById(R.id.uiLinkTV);
        keypadStatusRL = (RelativeLayout) findViewById(R.id.keypadStatusRL);
        statusKeypadLmTV = (TextView) findViewById(R.id.statusKeypadLmTV);
        statusKeypadLmButton = (Button) findViewById(R.id.statusKeypadLmButton);
        pb = (ProgressBar) findViewById(R.id.pb);
        keypadStatusRL.setVisibility(View.GONE);


        int phoneStatePermission = ContextCompat.checkSelfPermission(SdkTestLaunchActivity.this, Manifest.permission.READ_PHONE_STATE);
        if(phoneStatePermission == PackageManager.PERMISSION_GRANTED) {
           validateLicense();
        }
        else {
            ActivityCompat.requestPermissions(SdkTestLaunchActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE},READ_PHONE_STATE_REQUEST_CODE);
        }


        statusKeypadLmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keypadStatusRL.setVisibility(View.GONE);
            }
        });


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


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_PHONE_STATE_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                    validateLicense();
                } else {
                    Toast.makeText(SdkTestLaunchActivity.this, "Permission Denied, Exit..", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }


    /**
     *  License validation to be done once in application lifecycle.
     *  Resource download, Keypad initialization should follow only after a successful License validation call.
     */
    public void validateLicense() {
        keypadStatusRL.setVisibility(View.VISIBLE);

        /** API : License validation
         *  Params : Context, License validation base API url, License Key, App Id, ValidationCompleteListener Callback
         *  Callback : onValidationComplete(int statusCode, String statusMessage)
         */
        RevSDK.validateKey(SdkTestLaunchActivity.this.getApplicationContext(), TestConstants.LM_BASE_API_URL, TestConstants.SDK_TEST_API_KEY, TestConstants.SDK_TEST_APP_ID, new ValidationCompleteListener() {
            @Override
            public void onValidationComplete(int statusCode, String statusMessage) {
                Log.d("TAG" , "LICENSE VALIDATION COMPLETE : " + statusCode + " ," + statusMessage);
                pb.setVisibility(View.GONE);
                statusKeypadLmTV.setText("Response code : " + statusCode + "\n" + "Response message : " + statusMessage);
            }
        });
    }
}
