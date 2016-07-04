
This project demonstrates using of Reverie Android SDK API in your application.

Import this project to your Android Studio workspace. 
Before running the app please update the following constants with the one provided to you.

public static final String SDK_TEST_APP_ID = "com.sdktest";
public static final String SDK_TEST_API_KEY = "5ytz6LvbMwj3WWT27jbb4CxdUdF1zXOqe4nq";
public static final String LM_API_BASE_URL = "http://137.135.43.145:80";
public static final String LOCAL_TRANS_BASE_URL = "http://api.revup.reverieinc.com/v2";

Above credentials will be available once you register through RevUP in Reverie platform.
SDK_TEST_APP_ID is the package name of your application.
SDK_TEST_API_KEY is the API key for you once you register through RevUP.
LM_API_BASE_URL is the license manager Gateway URL which needs to be provided when you call Validate API
LOCAL_TRANS_BASE_URL is the Gateway URL for transliteration/localization API


Steps to integrate Keypad in your Application.

1. Add SDK jar file in your project
2. Put “btn.xml” and “color_bg.xml” into application’s res/drawable folder.
3. Put “attrs.xml” into res/values folder. If the application already has an existing attrs.xml, then add following declare-styleable element into         resource element of attrs.xml file.

     <declare-styleable name="mystyle">
         <attr name="suggestion" format="boolean"/>
     </declare-styleable>
4. Add Permission READ_PHONE_STATE into application’s manifest. If the application is targeting API level 23, handle runtime permission for the same.
5. Replace your EditText component with com.reverie.customcomponent.RevEditText
6. In your Activity validate the license first.

    Use license validation API and check for the result in callback method. The status code should be 1 for valid license.
7. Check for the resources for a selected language by calling 'checkResource' API. If it returns false, invoke 'downloadResources' API for that language. You can invoke these two API whenever a user selects a language.
8. If every steps are fine till now initiate the Keypad by calling 'initKeypad' API. This API has to be called from every Activity where Reverie Keypad is required.

And, you are done with the Keyapd integration.
 
