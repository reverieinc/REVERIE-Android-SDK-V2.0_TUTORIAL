package com.reverie.sdktutorial;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.reverie.lm.LM;
import com.reverie.manager.RevSDK;
import com.reverie.manager.ValidationCompleteListener;
import com.sdktest.R;

/**
 * Created by ranjan_mac on 23/05/16.
 */
public class SdkTestLM extends Activity {

    private EditText lmKeyET, lmPkgET;
    private TextView lmValidateTV, lmResponseTimeTV, lmOutputLocalTV, lmOutputServerTV, lmValidationStatusTV, lmCacheClearTV;
    private TextView lmKeyPickTV,lmPkgPickTV;

    private long startTime, endTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sdk_test_lm);

        lmKeyET = (EditText) findViewById(R.id.lmKeyET);
        lmPkgET = (EditText) findViewById(R.id.lmPkgET);
        lmValidateTV = (TextView) findViewById(R.id.lmValidateTV);
        lmOutputLocalTV = (TextView) findViewById(R.id.lmOutputLocalTV);
        lmOutputServerTV = (TextView) findViewById(R.id.lmOutputServerTV);
        lmValidationStatusTV = (TextView) findViewById(R.id.lmValidationStatusTV);
        lmResponseTimeTV = (TextView) findViewById(R.id.lmResponseTimeTV);
        lmCacheClearTV = (TextView) findViewById(R.id.lmCacheClearTV);
        lmKeyPickTV = (TextView) findViewById(R.id.lmKeyPickTV);
        lmPkgPickTV = (TextView) findViewById(R.id.lmPkgPickTV);

        //lmKeyET.setText(TestConstants.SDK_TEST_API_KEY);

        lmValidateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = lmKeyET.getText().toString();
                String pkg = lmPkgET.getText().toString();

                if(key != null && pkg != null && !key.isEmpty() && !pkg.isEmpty()) {
                    validateLM(key, pkg);
                }
                else {
                    Toast.makeText(SdkTestLM.this, "Enter Key and Pkg", Toast.LENGTH_SHORT).show();
                }
            }
        });


        lmCacheClearTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearLicenseCache();
            }
        });

        lmKeyPickTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyPkgPickDialog(1);
            }
        });

        lmPkgPickTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyPkgPickDialog(2);
            }
        });
    }


    public void validateLM(String key , String pkg) {

        lmOutputLocalTV.setText("");
        lmOutputServerTV.setText("");
        lmResponseTimeTV.setText("");
        lmValidationStatusTV.setText("Status: validating in process" );

        startTime = System.currentTimeMillis();

        RevSDK.validateKey(TestConstants.LM_API_BASE_URL, SdkTestLM.this, key, pkg, new ValidationCompleteListener() {
            @Override
            public void onValiodationComplete(int statusCode, String statusMessage) {
                Log.d("TAG" , "LICENSE VALIDATION COMPLETE : " + statusCode + " ," + statusMessage);

                endTime = System.currentTimeMillis();
                long diff = endTime - startTime;

                lmValidationStatusTV.setText("Status: validating process completed\n" + statusMessage );
                lmResponseTimeTV.setText(diff + " ms");
                lmOutputLocalTV.setText(LM.localResponseMsgForKeyValidation);
                lmOutputServerTV.setText(LM.serverResponseMsgForKeyValidation);
            }
        });
    }


    public void clearLicenseCache() {
        boolean status = RevSDK.clearLMCache(SdkTestLM.this);
        if(status) {
            Toast.makeText(SdkTestLM.this, "Data cleared", Toast.LENGTH_SHORT).show();
            lmOutputLocalTV.setText("");
            lmOutputServerTV.setText("");
            lmResponseTimeTV.setText("");
            lmValidationStatusTV.setText("");
            lmKeyET.setText("");
            lmPkgET.setText("");
        }
        else {
            Toast.makeText(SdkTestLM.this, "Clear data ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    public void showKeyPkgPickDialog(final int type) {
        final Dialog dialog = new Dialog(SdkTestLM.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_key_pick);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        RadioButton key_radio = (RadioButton) dialog.findViewById(R.id.key_radio);
        if(type == 1) {
            key_radio.setText(TestConstants.SDK_TEST_API_KEY);
        }
        else if(type == 2) {
            key_radio.setText(TestConstants.SDK_TEST_APP_ID);
        }

        key_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == 1) {
                    lmKeyET.setText(TestConstants.SDK_TEST_API_KEY);
                }
                else if(type == 2) {
                    lmPkgET.setText(TestConstants.SDK_TEST_APP_ID);
                }

                dialog.cancel();
            }
        });

        dialog.show();
    }



}
