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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.reverie.customcomponent.RevButton;
import com.reverie.customcomponent.RevCheckBox;
import com.reverie.customcomponent.RevEditText;
import com.reverie.customcomponent.RevRadioButton;
import com.reverie.customcomponent.RevTextView;
import com.reverie.customcomponent.RevToggleButton;
import com.reverie.manager.DownloadCompleteListener;
import com.reverie.manager.RevError;
import com.reverie.manager.RevSDK;
import com.reverie.manager.ValidationCompleteListener;
import com.sdktest.R;

/**
 * Created by ranjan_mac on 24/05/16.
 */
public class SdkTestUIComponents extends Activity {

    private Spinner componentLangSpinner;
    private int selectedLangId = 0;
    private String selectedLangName = "";

    private RelativeLayout uiComponentsRL, uiLMStatusRL, downloadStatusRL;
    private TextView statusKeypadLmTV, statusDownlaodResourceTV;
    private Button statusKeypadLmButton, statusDownlaodResourceButton;
    private ProgressBar pb, pb1;


    private TextView uiResetTV, uiSetTV;
    private RevEditText uiInputET;

    private RevTextView uiTV;
    private RevRadioButton uiRadio;
    private RevCheckBox uiCheck;
    private RevToggleButton uiToggle;
    private RevButton uiButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sdk_test_components);

        uiComponentsRL = (RelativeLayout) findViewById(R.id.uiComponentsRL);
        uiLMStatusRL = (RelativeLayout) findViewById(R.id.uiLMStatusRL);
        downloadStatusRL = (RelativeLayout) findViewById(R.id.downloadStatusRL);
        statusKeypadLmTV = (TextView) findViewById(R.id.statusKeypadLmTV);
        statusDownlaodResourceTV = (TextView) findViewById(R.id.statusDownlaodResourceTV);
        statusKeypadLmButton = (Button) findViewById(R.id.statusKeypadLmButton);
        statusDownlaodResourceButton = (Button) findViewById(R.id.statusDownlaodResourceButton);
        pb = (ProgressBar) findViewById(R.id.pb);
        pb1 = (ProgressBar) findViewById(R.id.pb1);

        uiSetTV = (TextView) findViewById(R.id.uiSetTV);
        uiResetTV = (TextView) findViewById(R.id.uiResetTV);
        uiInputET = (RevEditText) findViewById(R.id.uiInputET);

        uiTV = (RevTextView) findViewById(R.id.uiTV);
        uiRadio = (RevRadioButton) findViewById(R.id.uiRadio);
        uiCheck = (RevCheckBox) findViewById(R.id.uiCheck);
        uiToggle = (RevToggleButton) findViewById(R.id.uiToggle);
        uiButton = (RevButton) findViewById(R.id.uiButton);
        componentLangSpinner = (Spinner) findViewById(R.id.componentLangSpinner);

        uiComponentsRL.setVisibility(View.GONE);
        uiLMStatusRL.setVisibility(View.VISIBLE);

        RevSDK.validateKey(TestConstants.LM_API_BASE_URL, SdkTestUIComponents.this, TestConstants.SDK_TEST_API_KEY, TestConstants.SDK_TEST_APP_ID, new ValidationCompleteListener() {
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
                uiComponentsRL.setVisibility(View.VISIBLE);
                uiLMStatusRL.setVisibility(View.GONE);

                initLanguageSpinner();

                uiSetTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String input = uiInputET.getText().toString();
                        if(input != null && !input.isEmpty()) {
                            setUIComponents(input);
                        }
                        else {
                            Toast.makeText(SdkTestUIComponents.this, "Enter text to set components", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                uiResetTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resetUIComponents();
                    }
                });
            }
        });
    }

    public void initLanguageSpinner() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, TestConstants.langNamesArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        componentLangSpinner.setAdapter(spinnerArrayAdapter);

        componentLangSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        boolean status = RevSDK.initKeypad(SdkTestUIComponents.this, selectedLangId);
        if(RevSDK.checkResource(SdkTestUIComponents.this, selectedLangId)) {
            // do nothing, already available
            Log.d("TAG", "RESOURCE AVAILABLE FOR : " + selectedLangId );
        }
        else {
            showDownloadResourceConfirmDialog();
        }
    }

    public void showDownloadResourceConfirmDialog() {
        new AlertDialog.Builder(SdkTestUIComponents.this)
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

        RevSDK.downloadResources(SdkTestUIComponents.this, selectedLangId, new DownloadCompleteListener() {
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


    public void resetUIComponents() {
        uiTV.setText("This is a Rev TextView");
        uiRadio.setText("Radio 2");
        uiCheck.setText("Checkbox 2");
        uiToggle.setTextOff("TOGGLE OFF");
        uiToggle.setTextOn("TOGGLE ON");
        uiToggle.setChecked(uiToggle.isChecked());
        uiButton.setText("Button 2");
    }

    public void setUIComponents(String input) {
        uiTV.setText(input);
        uiRadio.setText(input);
        uiCheck.setText(input);
        uiToggle.setTextOff("TOGGLE OFF");
        uiToggle.setTextOn(input);
        uiToggle.setChecked(uiToggle.isChecked());
        uiButton.setText(input);
    }
}
