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
import com.reverie.lm.LM;
import com.reverie.manager.LangResourceInitCompleteListener;
import com.reverie.manager.RevSDK;
import com.reverie.manager.RevStatus;
import com.reverie.manager.ValidationCompleteListener;
import com.sdktest.R;

/**
 * Created by ranjan_mac on 24/05/16.
 */
public class SdkTestUIComponents extends Activity {

    private Spinner componentLangSpinner;
    private int selectedLangId = 0;
    private String selectedLangName = "";

    private RelativeLayout uiComponentsRL, downloadStatusRL;
    private TextView statusDownlaodResourceTV;
    private Button statusDownlaodResourceButton;
    private ProgressBar pb1;


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
        downloadStatusRL = (RelativeLayout) findViewById(R.id.downloadStatusRL);
        statusDownlaodResourceTV = (TextView) findViewById(R.id.statusDownlaodResourceTV);
        statusDownlaodResourceButton = (Button) findViewById(R.id.statusDownlaodResourceButton);
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

        uiComponentsRL.setVisibility(View.VISIBLE);

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

        /**
         *  initLangResources API downloads resources, if NOT downloaded already (font and dictionary) from Reverie Server for requested Langauge, And initialize resources
         *  API : initLangResources
         *  Params : Resource Doenload Base Api Url, Langauge id, LangResourceInitCompleteListener callback
         *  Callback : onLangResourceInitComplete (int language code, RevStatus status)
         */
        RevSDK.initLangResources(TestConstants.RESOURCE_DOWNLOAD_BASE_API_URL, selectedLangId, new LangResourceInitCompleteListener() {
            @Override
            public void onLangResourceInitComplete(int i, RevStatus revStatus) {

                Log.d("TAG", "INIT RESOURCE COMPLETE:  Lang code = " + i + " , Status = " + revStatus.getStatusMessage());

                if (revStatus.getStatusCode() == RevStatus.SUCCESS) {
                    /**
                     *  InitKeypad API parse the provided Activity and identify Custom component "RevEditText" to avail kepad.
                     *  API : initKeypad
                     *  Params : int Langauge id
                     *  Return : boolean Status
                     */
                    boolean status = RevSDK.initKeypad(SdkTestUIComponents.this, selectedLangId);
                }
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
