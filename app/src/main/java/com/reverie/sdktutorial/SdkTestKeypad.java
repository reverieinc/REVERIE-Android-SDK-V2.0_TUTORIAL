package com.reverie.sdktutorial;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.reverie.manager.DownloadCompleteListener;
import com.reverie.manager.RevError;
import com.reverie.manager.RevSDK;
import com.reverie.manager.ValidationCompleteListener;
import com.sdktest.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by ranjan_mac on 24/05/16.
 */
public class SdkTestKeypad extends Activity {

    private Spinner keypadLangSpinner;
    private RelativeLayout keypadEditTextRL, keypadStatusRL, downloadStatusRL;
    private TextView statusKeypadLmTV, statusDownlaodResourceTV;
    private Button statusKeypadLmButton, statusDownlaodResourceButton;
    private ProgressBar pb, pb1;

    private ArrayList<Lang> langList = new ArrayList<>();
    private int selectedLangId = 0;
    private String selectedLangName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sdk_test_keypad);

        keypadLangSpinner = (Spinner) findViewById(R.id.keypadLangSpinner);
        keypadEditTextRL = (RelativeLayout) findViewById(R.id.keypadEditTextRL);
        keypadStatusRL = (RelativeLayout) findViewById(R.id.keypadStatusRL);
        downloadStatusRL = (RelativeLayout) findViewById(R.id.downloadStatusRL);
        statusKeypadLmTV = (TextView) findViewById(R.id.statusKeypadLmTV);
        statusDownlaodResourceTV = (TextView) findViewById(R.id.statusDownlaodResourceTV);
        statusKeypadLmButton = (Button) findViewById(R.id.statusKeypadLmButton);
        statusDownlaodResourceButton = (Button) findViewById(R.id.statusDownlaodResourceButton);
        pb = (ProgressBar) findViewById(R.id.pb);
        pb1 = (ProgressBar) findViewById(R.id.pb1);


        keypadEditTextRL.setVisibility(View.GONE);
        keypadStatusRL.setVisibility(View.VISIBLE);

        RevSDK.validateKey(TestConstants.LM_API_BASE_URL, SdkTestKeypad.this, TestConstants.SDK_TEST_API_KEY, TestConstants.SDK_TEST_APP_ID, new ValidationCompleteListener() {
            @Override
            public void onValiodationComplete(int statusCode, String statusMessage) {
                Log.d("TAG" , "LICENSE VALIDATION COMPLETE : " + statusCode + " ," + statusMessage);
                pb.setVisibility(View.GONE);
                statusKeypadLmTV.setText("Response code : " + statusCode + "\n" + "Response message : " + statusMessage);
            }
        });

        statusKeypadLmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keypadEditTextRL.setVisibility(View.VISIBLE);
                keypadStatusRL.setVisibility(View.GONE);

                initLanguageSpinner();
            }
        });
    }


    public void initLanguageSpinner() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, TestConstants.langNamesArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        keypadLangSpinner.setAdapter(spinnerArrayAdapter);

        keypadLangSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        String str = TestConstants.langIdsArray[pos] + " , " + TestConstants.langNamesArray[pos];
        //Toast.makeText(SdkTestKeypad.this, "Select Lang: " + str , Toast.LENGTH_SHORT).show();

        boolean status = RevSDK.initKeypad(SdkTestKeypad.this, selectedLangId);

        if(RevSDK.checkResource(SdkTestKeypad.this, selectedLangId)) {
            // do nothing, already available
            Log.d("TAG", "RESOURCE AVAILABLE FOR : " + selectedLangId );
        }
        else {
            showDownloadResourceConfirmDialog();
        }
    }


    public void showDownloadResourceConfirmDialog() {
        new AlertDialog.Builder(SdkTestKeypad.this)
                .setTitle("Download Resource")
                .setMessage("Resource not available for selected Language.")
                .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        downloadLangresource();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void downloadLangresource() {
        statusDownlaodResourceTV.setText("");
        downloadStatusRL.setVisibility(View.VISIBLE);
        pb1.setVisibility(View.VISIBLE);

        RevSDK.downloadResources(SdkTestKeypad.this, selectedLangId, new DownloadCompleteListener() {
            @Override
            public void onDownloadComplete(int langCode, boolean font, boolean dict, RevError errorMsg) {
                Log.d("TAG", "DOWNLOAD COMPLETE KEYPAD:  "+ langCode + " , " + font + ", " + dict + ", " + errorMsg.getErrorMessage());
                pb1.setVisibility(View.GONE);
                String status =  "Language Code: " + langCode + "\n"
                        + "Font download status : " + font + "\n"
                        + "Dictionary download status :" + dict + "\n"
                        + "Status Message : " + errorMsg.getErrorMessage();

                statusDownlaodResourceTV.setText(status);
                statusDownlaodResourceButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadStatusRL.setVisibility(View.GONE);
                    }
                });

            }
        });
    }

}
