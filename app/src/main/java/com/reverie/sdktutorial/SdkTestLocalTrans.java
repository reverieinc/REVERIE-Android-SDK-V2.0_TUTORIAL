package com.reverie.sdktutorial;

import android.app.Activity;
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

import com.reverie.customcomponent.RevEditText;
import com.reverie.lm.LM;
import com.reverie.localization.RevLocalization;
import com.reverie.localization.SimpleLocalizationListener;
import com.reverie.manager.LangResourceInitCompleteListener;
import com.reverie.manager.RevSDK;
import com.reverie.manager.RevStatus;
import com.reverie.manager.ValidationCompleteListener;
import com.reverie.search.SearchAssistListener;
import com.reverie.transliterationsonline.RevTransliterationOnline;
import com.reverie.transliterationsonline.SimpleSuggestionListener;
import com.reverie.transliterationsonline.SimpleTransliterationListener;
import com.sdktest.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ranjan_mac on 26/05/16.
 */
public class SdkTestLocalTrans extends Activity {

    private RelativeLayout localTransOutRL;

    private RevEditText localTransInputET;
    private Spinner sourceLangSpinner, targetLangSpinner, transTypeSpinner, domainTypeSpinner;
    private TextView localTransSubmitTV, localResultTV;
    private ProgressBar pb;

    private String inputText = "";
    private int selectedSourceLangId = 0;
    private int selectedTargetLangId = 0;
    private int selectedTransTypeId = 0;
    private int selectedDomainTypeId = 0;

    private String selectedSourceLangName = "";
    private String selectedTargetLangName = "";
    private String selectedTransTypeName = "";
    private String selectedDomainTypeName = "";
    private String selectedDomain = "";

    String resultLocalTrans  = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sdk_test_local_trans);

        localTransOutRL = (RelativeLayout) findViewById(R.id.localTransOutRL);
        localTransInputET = (RevEditText) findViewById(R.id.localTransInputET);
        sourceLangSpinner = (Spinner) findViewById(R.id.sourceLangSpinner);
        targetLangSpinner = (Spinner) findViewById(R.id.targetLangSpinner);
        transTypeSpinner = (Spinner) findViewById(R.id.transTypeSpinner);
        domainTypeSpinner = (Spinner) findViewById(R.id.domainTypeSpinner);
        localTransSubmitTV = (TextView) findViewById(R.id.localTransSubmitTV);
        localResultTV = (TextView) findViewById(R.id.localResultTV);
        pb = (ProgressBar) findViewById(R.id.pb);


        localTransOutRL.setVisibility(View.VISIBLE);
        initLanguageSpinner();
        initTransTypeSpinner();
        initDomainSpinner();

        localTransSubmitTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });

    }


    public void initLanguageSpinner() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, TestConstants.langNamesArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceLangSpinner.setAdapter(spinnerArrayAdapter);
        targetLangSpinner.setAdapter(spinnerArrayAdapter);

        sourceLangSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long id) {
                onSpinnerSourceLangSelected(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        targetLangSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long id) {
                onSpinnerTargetLangSelected(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
    }

    public void initTransTypeSpinner() {
        final String[] transTypeArray = new String[]{"Localisation", "Transliteration", "Suggestion"/*, "Search"*/};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, transTypeArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transTypeSpinner.setAdapter(spinnerArrayAdapter);

        transTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long id) {
                selectedTransTypeId = pos + 1;
                selectedTransTypeName = transTypeArray[pos];
                initDomainSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

    }

    public void initDomainSpinner() {
        final int[] domainIdLocalisationArray = new int[]{1,2,3,4,5,6,7,8};
        final int[] domainIdTransliterationArray = new int[]{1,3,19,4,6,7,9,12};
        final String[] domainNameLocalisationArray = new String[]{"General", "Travel", "Ecommerce", "Music Entertain", "Banking", "Grocery", "Education Jobs", "Medical"};
        final String[] domainNameTransliterationArray = new String[]{"Default", "Entertainment", "Names", "English", "Banking", "Brands", "Addresses", "Ecommerce"};

        if(selectedTransTypeId == 1) {
            // Localisation
            localTransInputET.setHint("For multiple use comma");
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, domainNameLocalisationArray);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            domainTypeSpinner.setAdapter(spinnerArrayAdapter);

            domainTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long id) {
                    selectedDomainTypeId = domainIdLocalisationArray[pos];
                    selectedDomainTypeName = domainNameLocalisationArray[pos];
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }

            });

        }
        else if(selectedTransTypeId == 2 || selectedTransTypeId == 3) {
            // Transliteration
            if(selectedTransTypeId == 2) {
                localTransInputET.setHint("For multiple use comma");
            }
            else if(selectedTransTypeId == 3) {
                localTransInputET.setHint("Enter text");
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, domainNameTransliterationArray);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                domainTypeSpinner.setAdapter(spinnerArrayAdapter);

                domainTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long id) {
                        selectedDomainTypeId = domainIdTransliterationArray[pos];
                        selectedDomainTypeName = domainNameTransliterationArray[pos];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }

                });
            }
            else if(selectedTransTypeId == 4) {
                // Search
                localTransInputET.setHint("Enter text");
            }



        }
    }

    public void onSpinnerSourceLangSelected(int pos) {
        selectedSourceLangId = TestConstants.langIdsArray[pos];
        selectedSourceLangName = TestConstants.langNamesArray[pos];

        /**
         *  initLangResources API downloads resources, if NOT downloaded already (font and dictionary) from Reverie Server for requested Langauge, And initialize resources
         *  API : initLangResources
         *  Params : Resource Doenload Base Api Url, Langauge id, LangResourceInitCompleteListener callback
         *  Callback : onLangResourceInitComplete (int language code, RevStatus status)
         */
        RevSDK.initLangResources(TestConstants.RESOURCE_DOWNLOAD_BASE_API_URL, selectedSourceLangId, new LangResourceInitCompleteListener() {
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
                    boolean status = RevSDK.initKeypad(SdkTestLocalTrans.this, selectedSourceLangId);
                }
            }
        });
    }

    public void onSpinnerTargetLangSelected(int pos) {
        selectedTargetLangId = TestConstants.langIdsArray[pos];
        selectedTargetLangName = TestConstants.langNamesArray[pos];

    }


    public void onSubmit() {
        String inputText = localTransInputET.getText().toString();
        if(inputText != null && !inputText.isEmpty()) {
            if(selectedTransTypeId == 1) {
                // LOCALISATION
                callLocalisationApi(inputText, TestConstants.REVUP_LOCALIZATION_API_ENDPOINT);
            }
            else if(selectedTransTypeId == 2) {
                //TRANSILETARATION
                callTransliterationApi(inputText, TestConstants.REVUP_TRANSLITERATION_API_ENDPOINT);
            }
            else if(selectedTransTypeId == 3) {
                //TRANSILETARATION SUGGESTION
                callTransliterationSuggestionApi(inputText, 5, TestConstants.REVUP_TRANSLITERATION_API_ENDPOINT);
            }
            else if(selectedTransTypeId == 4) {
                // SEARCH ASSIST
                //callSearchAssistApi(inputText, TestConstants.REVUP_SEARCH_API_ENDPOINT);
            }
        }
        else {
            Toast.makeText(SdkTestLocalTrans.this, "Enter Input Text", Toast.LENGTH_SHORT).show();
        }
    }


    public void callLocalisationApi(String input, String apiBaseUrl) {
        Log.d("TAG", "LOCALISATION : API CALLING >>>> " + apiBaseUrl);
        resultLocalTrans = "";
        localResultTV.setText("");
        pb.setVisibility(View.VISIBLE);
        String[] inputStringArray = input.split(",");


        /**
         *  getLocalizedText API returns localized text result for input text and provided target langauge via Reverie Server Network call
         *  API : getLocalizedText
         *  Params : Activity, Localization API Url(String), API key(String), App Id(String), Input text(String[]), Domain Id (int), Target Langauge Id (int), Source Langauge Id (int), SimpleLocalizationListener Callback
         *  Callback : onResult(int responseCode, String responseJson, HashMap<String, String> result)
         */
        RevSDK.getLocalizedText(SdkTestLocalTrans.this, apiBaseUrl, TestConstants.SDK_TEST_API_KEY, TestConstants.SDK_TEST_APP_ID, inputStringArray, selectedDomainTypeId, selectedTargetLangId, selectedSourceLangId, new SimpleLocalizationListener() {
            @Override
            public void onResult(int responseCode, String responseJson, HashMap<String, String> result) {
                Log.d("TAG", "LOCALISATION : ON RESULT >>>> ");
                pb.setVisibility(View.GONE);
                if(result != null) {
                    for (Map.Entry<String,String> entry : result.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        Log.d("TAG", "KEY: " + key + " , VALUE: " + value);
                        resultLocalTrans = resultLocalTrans + key + " = " + value + " , ";
                    }
                }

                // DISPPLAY RESULT
                Log.d("TAG", "LOCALISATION RESULT:" + responseCode + "," + responseJson);

                localResultTV.setText("Response Code: " + responseCode + "\n" +
                        "Response Json: " + responseJson + "\n\n" +
                        "RESULT : " + resultLocalTrans);
            }
        });
    }


    public void callTransliterationApi(String input, String apiBaseUrl) {
        Log.d("TAG", "TRANSILETARTION : API CALLING >>>> ");
        resultLocalTrans = "";
        localResultTV.setText("");
        pb.setVisibility(View.VISIBLE);
        String[] inputStringArray = input.split(",");

        /**
         *  getTransliteratedText API returns Transliterated text result for input text and provided target langauge via Reverie Server Network call
         *  API : getTransliteratedText
         *  Params : Activity, Transaltion API Url(String), API key(String), App Id(String), Input text(String[]), Domain Id (int), Target Langauge Id (int), Source Langauge Id (int), SimpleTransliterationListener Callback
         *  Callback : onResult(int responseCode, String responseJson, HashMap<String, String> result)
         */
        RevSDK.getTransliteratedText(SdkTestLocalTrans.this, apiBaseUrl, TestConstants.SDK_TEST_API_KEY, TestConstants.SDK_TEST_APP_ID,inputStringArray, selectedDomainTypeId, selectedTargetLangId, selectedSourceLangId, new SimpleTransliterationListener() {
            @Override
            public void onResult(int responseCode, String responseJson, HashMap<String, String> result) {
                Log.d("TAG", "TRANSILETARATION : ON RESULT >>>> ");
                pb.setVisibility(View.GONE);
                if(result != null) {
                    for (Map.Entry<String,String> entry : result.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        Log.d("TAG", "KEY: " + key + " , VALUE: " + value);
                        resultLocalTrans = resultLocalTrans + key + " = " + value+ " , ";
                    }
                }

                // DISPPLAY RESULT
                Log.d("TAG", "TRANSILETARATION RESULT:" + responseCode + "," + responseJson);

                localResultTV.setText("Response Code: " + responseCode + "\n" +
                        "Response Json: " + responseJson + "\n\n" +
                        "RESULT : " + resultLocalTrans);
            }
        });
    }


    public void callTransliterationSuggestionApi(String input, int suggestionLimit, String apiBaseUrl) {
        Log.d("TAG", "TRANSILETARTION SUGGESTION: API CALLING >>>> ");
        resultLocalTrans = "";
        localResultTV.setText("");
        pb.setVisibility(View.VISIBLE);

        /**
         *  getSuggestedText API returns multiple Transliterated text result for input text and provided target langauge via Reverie Server Network call
         *  API : getSuggestedText
         *  Params : Activity, Transaltion API Url(String), API key(String), App Id(String), Input text(String), Domain Id (int), Target Langauge Id (int), Source Langauge Id (int), Number of suggestions (int), SimpleSuggestionListener Callback
         *  Callback : onResult(int responseCode, String responseJson, ArrayList<String> result)
         */
        RevSDK.getSuggestedText(SdkTestLocalTrans.this, apiBaseUrl, TestConstants.SDK_TEST_API_KEY, TestConstants.SDK_TEST_APP_ID, input, selectedDomainTypeId, selectedTargetLangId, selectedSourceLangId, suggestionLimit, new SimpleSuggestionListener() {
            @Override
            public void onResult(int responseCode, String responseJson, ArrayList<String> result) {
                Log.d("TAG", "TRANSILETARATION SUGGESTION: ON RESULT >>>> ");
                pb.setVisibility(View.GONE);

                if(result != null) {
                    for(String s : result) {
                        resultLocalTrans = resultLocalTrans + s + ", ";
                    }
                }

                // DISPPLAY RESULT
                Log.d("TAG", "TRANSILETARATION SUGGESTION RESULT:" + responseCode + "," + responseJson);

                localResultTV.setText("Response Code: " + responseCode + "\n" +
                        "Response Json: " + responseJson + "\n\n" +
                        "RESULT : " + resultLocalTrans);
            }
        });
    }

/*
    *//**
     *  callSearchAssistApi API returns English text result for input localized text via Reverie Server Network call
     *  API : getSearchAssistText
     *  Params : Activity, Search Assist API Url(String), API key(String), App Id(String), Input text(String), Source Langauge Id (int), SearchAssistListener Callback
     *  Callback : onResult(int responseCode, String responseJson, String result)
     *//*
    public void callSearchAssistApi(String input, String apiBaseUrl) {
        Log.d("TAG", "SEARCH ASSIST: API CALLING >>>> ");
        resultLocalTrans = "";
        localResultTV.setText("");
        pb.setVisibility(View.VISIBLE);

        RevSDK.getSearchAssistText(apiBaseUrl, TestConstants.SDK_TEST_API_KEY, TestConstants.SDK_TEST_APP_ID, input, selectedSourceLangId, new SearchAssistListener() {
            @Override
            public void onResult(int responseCode, String responseJson, String result) {
                Log.d("TAG", "SEARCH ASSIST : ON RESULT >>>> ");
                pb.setVisibility(View.GONE);

                // DISPPLAY RESULT
                Log.d("TAG", "SEARCH ASSIST RESULT:" + responseCode + "," + responseJson);

                localResultTV.setText("Response Code: " + responseCode + "\n" +
                        "Response Json: " + responseJson + "\n\n" +
                        "RESULT : " + result);
            }
        });
    }*/


}
