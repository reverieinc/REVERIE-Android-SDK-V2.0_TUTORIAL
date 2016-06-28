package com.reverie.sdktutorial;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.reverie.manager.DownloadCompleteListener;
import com.reverie.manager.RevError;
import com.reverie.manager.RevSDK;
import com.reverie.manager.ValidationCompleteListener;
import com.sdktest.R;

import java.util.ArrayList;

/**
 * Created by ranjan_mac on 23/05/16.
 */
public class SdkTestDownloadResource extends Activity {

    private Spinner dmLangSpinner;
    private ArrayList<Lang> langList = new ArrayList<>();
    private int selectedLangId = 0;
    private String selectedLangName = "";

    private TextView langResourceCheckTV , langResourceDownlaodTV;
    private TextView dmStatusTV, dmCheckResultlTV, dmDownloadResultTV;

    private long t_start, t_end = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sdk_test_download_resource);

        langResourceCheckTV = (TextView) findViewById(R.id.langResourceCheckTV);
        langResourceDownlaodTV = (TextView) findViewById(R.id.langResourceDownlaodTV);
        dmStatusTV = (TextView) findViewById(R.id.dmStatusTV);
        dmCheckResultlTV = (TextView) findViewById(R.id.dmCheckResultlTV);
        dmDownloadResultTV = (TextView) findViewById(R.id.dmDownloadResultTV);
        dmLangSpinner = (Spinner) findViewById(R.id.dmLangSpinner);

        initLanguageSpinner();


        RevSDK.validateKey(TestConstants.LM_API_BASE_URL, SdkTestDownloadResource.this, TestConstants.SDK_TEST_API_KEY, TestConstants.SDK_TEST_APP_ID, new ValidationCompleteListener() {
            @Override
            public void onValiodationComplete(int statusCode, String statusMessage) {
                Log.d("TAG" , "LICENSE VALIDATION COMPLETE : " + statusCode + " ," + statusMessage);
            }
        });


        langResourceCheckTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkResource();
            }
        });

        langResourceDownlaodTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadResource();
            }
        });

    }

    public void initLanguageSpinner() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, TestConstants.langNamesArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dmLangSpinner.setAdapter(spinnerArrayAdapter);

        dmLangSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long id) {
                onSpinnerLangSelected(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
    }

    public void onSpinnerLangSelected(int pos) {
        selectedLangId = TestConstants.langIdsArray[pos];
        selectedLangName = TestConstants.langNamesArray[pos];
        dmStatusTV.setText("");
        dmCheckResultlTV.setText("");
        dmDownloadResultTV.setText("");
        String str = TestConstants.langIdsArray[pos] + " , " + TestConstants.langNamesArray[pos];
        Toast.makeText(SdkTestDownloadResource.this, "Select Lang: " + str , Toast.LENGTH_SHORT).show();
    }


    public void checkResource() {
        dmStatusTV.setText("");
        dmCheckResultlTV.setText("");
        dmDownloadResultTV.setText("");

        boolean result = RevSDK.checkResource(SdkTestDownloadResource.this, selectedLangId);
        if(result) {
            dmCheckResultlTV.setText("Resources available for Lang : " + selectedLangId + ", " + selectedLangName);
        }
        else {
            dmCheckResultlTV.setText("Resources NOT available for Lang : " + selectedLangId + ", " + selectedLangName);
        }
    }

    public void downloadResource() {
        dmStatusTV.setText("");
        dmCheckResultlTV.setText("");
        dmDownloadResultTV.setText("");

        boolean result = RevSDK.checkResource(SdkTestDownloadResource.this, selectedLangId);
        if(result) {
            dmDownloadResultTV.setText("Resources already downloaded for Lang : " + selectedLangId + ", " + selectedLangName);
        }
        else {
            dmDownloadResultTV.setText("");
            dmStatusTV.setText("Downloading ...." + selectedLangName);
            Log.d("TAG", "DOWNLOAD STARTS");
            t_start = System.currentTimeMillis();

            RevSDK.downloadResources(SdkTestDownloadResource.this, selectedLangId, new DownloadCompleteListener() {
                @Override
                public void onDownloadComplete(int langCode, boolean font, boolean dict, RevError errorMsg) {
                    Log.d("TAG", "DOWNLOAD COMPLETE :  "+ langCode + " , " + font + ", " + dict + ", " + errorMsg.getErrorMessage());
                    dmStatusTV.setText("Download API completed for Lang :  " + selectedLangName);
                    t_end = System.currentTimeMillis();
                    long time = (t_end - t_start);


                    String str = "Response time : " + time + " ms" + "\n"
                                    + "Language :  " + selectedLangName + "\n"
                                    + "Is Font downloaded : " + font + "\n"
                                    + "Is Dictionary downloaded : " + dict + "\n"
                                    + "Status : " + errorMsg.getErrorMessage();
                    dmDownloadResultTV.setText(str);
                }
            });
        }
    }

}
