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
    private RelativeLayout keypadEditTextRL,downloadStatusRL;
    private TextView statusDownlaodResourceTV;
    private Button statusDownlaodResourceButton;
    private ProgressBar pb1;

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
        downloadStatusRL = (RelativeLayout) findViewById(R.id.downloadStatusRL);
        statusDownlaodResourceTV = (TextView) findViewById(R.id.statusDownlaodResourceTV);
        statusDownlaodResourceButton = (Button) findViewById(R.id.statusDownlaodResourceButton);
        pb1 = (ProgressBar) findViewById(R.id.pb1);


        keypadEditTextRL.setVisibility(View.VISIBLE);
        initLanguageSpinner();

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

        /**
         *  To initialize language resources and keypad with a particular langauge,
         *  Need to call download resources API, which will download resources if required and initialize resources.
         *  On successfull resource initialization, init keypad API  should initiate.
         */

        initializeLangaugeResource(selectedLangId);
    }


    public void initializeLangaugeResource(int langId) {
        statusDownlaodResourceTV.setText("");
        downloadStatusRL.setVisibility(View.VISIBLE);
        pb1.setVisibility(View.VISIBLE);

        /**
         *  downloadResources API downloads resources, if required (font and dictionary) from Reverie Server for requested Langauge.
         *  API : downloadResources
         *  Params : Resource Doenload Base Api Url, Langauge id, DownloadCompleteListener callback
         *  Callback : onDownloadComplete (int language code, boolean Font download status, boolean Dictionary download status, RevError Error Message
         */
        RevSDK.downloadResources(TestConstants.RESOURCE_DOWNLOAD_BASE_API_URL, selectedLangId, new DownloadCompleteListener() {
            @Override
            public void onDownloadComplete(int langCode, boolean font, boolean dict, RevError status) {

                Log.d("TAG", "DOWNLOAD COMPLETE KEYPAD:  "+ langCode + " , " + font + ", " + dict + ", " + status.getErrorMessage());

                String displayMsg =  "Language Code: " + langCode + "\n"
                        + "Font download status : " + font + "\n"
                        + "Dictionary download status :" + dict + "\n"
                        + "Status Message : " + status.getErrorMessage();

                pb1.setVisibility(View.GONE);
                statusDownlaodResourceTV.setText(displayMsg);
                statusDownlaodResourceButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadStatusRL.setVisibility(View.GONE);

                        /**
                         *  On getting callback onDownloadComplete, call init keypad API.
                         *  (If in above said callback 2nd parameter (boolean font) is false, appearance and size of the keyboard fonts may differ)
                         *  (If in above said callback 3rd parameter (boolean dict) is false, Suggestions may not display for input characters)
                         */


                        /**
                         *  InitKeypad API parse the provided Activity and identify Custom component "RevEditText" to avail kepad.
                         *  API : initKeypad
                         *  Params : int Langauge id
                         *  Return : boolean Status
                         */
                        boolean status = RevSDK.initKeypad(SdkTestKeypad.this, selectedLangId);
                    }
                });




            }
        });
    }

}
