
This project demonstrates using of Reverie Android SDK API in your application.

Import this project to your Android Studio workspace. 

Before running the app please update the following constants in TestConstants.java file.

    public static final String SDK_TEST_APP_ID = "com.sdktest";
    public static final String SDK_TEST_API_KEY = "Mi0gQLEoLXhn70yrrVyh2ti5AYGgFhwGwLWZ";

    public static final String REVUP_LOCALIZATION_API_ENDPOINT = "https://api-revup.reverieinc.com/apiman-gateway/ReverieMobilitySdk/localization/1.0";
    public static final String REVUP_TRANSLITERATION_API_ENDPOINT = "https://api-revup.reverieinc.com/apiman-gateway/ReverieMobilitySdk/transliteration/1.0";
    public static final String REVUP_SEARCH_API_ENDPOINT = "https://api-revup.reverieinc.com/apiman-gateway/ReverieMobilitySdk/search/1.0";
    
Above credentials will be available once you register through RevUP in Reverie platform.
link to revUp -- http://beta.revup.reverieinc.com.

SDK_TEST_APP_ID is the package name of your application.
SDK_TEST_API_KEY is the API key for you once you register through RevUP.
REVUP_LOCALIZATION_API_ENDPOINT - Endpoint to Reverie localization server
REVUP_TRANSLITERATION_API_ENDPOINT - Endpoint to Reverie Transliteration server.
REVUP_SEARCH_API_ENDPOINT - Endpoint to Reverie Search assist server.

Once these variables are updated run the project.


To integrate the SDK(library jar) in your existing application follow the steps below.

Following integration steps covers all the API integration. Developers don’t need to follow all steps if they are not using all APIs. Step 1 and 2 are mondetory.
For Localization API see step 3.
For Transliteration API see step 4.
For Search assist API see step 5.
If you are using keypad API see step 6 to step 9.


1.	Add following permissions in your manifest file
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.VIBRATE" />

If your target platform is 20 or above, you have to implement runtime permission as mentioned in official Android developers doc.

2.	Place the library jar into your project ‘libs’ folder and add it in app dependency. If ‘libs’ folder is not available, create it under ‘yourappfolder/src/’

3.	Localization API
The Localization API translates an input string (a word or a phrase) in the script of one language into the most commonly understood interpretation of that string in the script of another language.

Below is the API signature:
Package : com.reverie.manager
Class : RevSDK
public static void  getLocalizedText( Context context, String apiBaseUrl, String apiKey, String appId, String []inArray, int domain, int targetLanguage, int originLanguage, SimpleLocalizationListener simpleLocalizationListener)

context – the application context
apiBaseUrl- Reverie localization server endpoint. You will find it in the profile section of your revUp profile.
apiKey- license key. See the profile section of your revUp profile.
appId – Your registered application id
inArray – Array of strings needs to be localized
domain - Language localization is contextual. At times, the same phrase might have different meanings when used in different contexts. Providing an integer value for the domain helps in better localization. See Appendix for domain list.
targetLanguage - Selected language code. Language code can be found in RevLangConstants class. 
originLanguage – original language code. By default keep it English
simpleLocalizationListener – Callback interface. Developer will get the result through this interface. The callback returns result in json format as well as map format. User can use any one format of data as per their convenience. Below is the interface structure.

Package : com.reverie.localization
public interface SimpleLocalizationListener {
    public void onResult(int responseCode, String responseJson, HashMap<String, String> resultMap);
}
 responseCode – server response code.
responseJson –  result  Json in string format
resultMap –  result in map format. Each input string is the key and corresponding localized string is the value.


4.	Transliteration API
The Transliteration API converts an input string (a word or a phrase) in the script of one language into the script of another language without altering the grammar and arrangement of words in any way.

Below is the API signature:
Package : com.reverie.manager
Class : RevSDK
public static void  getTransliteratedText
 ( Context context, String apiBaseUrl, String apiKey, String appId, String []inArray, int domain, int targetLanguage, int originLanguage, SimpleTransliterationListener simpleTransliterationListener)

context – the application context
apiBaseUrl- Reverie transliteration server endpoint. You will find it in the profile section of your revUp profile.
apiKey- license key. See the profile section of your revUp profile.
appId – Your registered application id
inArray – Array of strings needs to be transliterated
domain - Language transliteration  is contextual. At times, the same phrase might have different meanings when used in different contexts. Providing an integer value for the domain helps in better transliteration. See Appendix for domain list.
targetLanguage - Selected language code. Language code can be found in RevLangConstants class. 
originLanguage – original language code. By default keep it English
simpleTransliterationListener – Callback interface. Developer will get the result through this interface. The callback returns result in json format as well as map format. User can use any one format of data as per their convenience. Below is the interface structure.

Package : com.reverie.transliterationsonline
public interface SimpleTransliterationListener {
   public void onResult(int responseCode, String responseJson, HashMap<String, String> resultMap);
} 
 responseCode – server response code.
responseJson –  result  Json in string format
resultMap –  result in map format. Each input string is the key and corresponding transliterated string is the value.

5.	Search assist API
The Search API converts local language queries into English language ones. 
Package : com.reverie.manager
Class : RevSDK
public static void getSearchAssistText(Context context, String apiBaseUrl, String apiKey, String appId, String inString, int language, SearchAssistListener searchAssistListener)

context – the application context
apiBaseUrl- Reverie search assist server endpoint. You will find it in the profile section of your revUp profile.
apiKey- license key. See the profile section of your revUp profile.
appId – Your registered application id
inString – native language string to be converted
searchAssistListener – Callback interface. Developer will get the result through this interface. The callback returns result in json format as well as string format. User can use any one format of data as per their convenience. Below is the interface structure.

Package : com.reverie.search
public interface SearchAssistListener {
    void onResult(int responseCode, String responseJson, String result);
}
 responseCode – server response code.
responseJson –  result  Json in string format
result –  result in English


6.	If you are inflating layouts from xml file then open your layout files. Find all ‘EditText’ components in your layout files and replace them with ‘com.reverie.customcomponent.RevEditText’. Build the project to check if everything is fine. If you get error like ‘classnotfoundexception’ check if the jar is in your project build path.

7.	If you are creating layout programmatically, replace all ‘EditText’ instance with ‘com.reverie.customcomponent.RevEditText’.

8.	Replace all ‘EditText’ reference with ‘com.reverie.customcomponent.RevEditText’ in your project.

9.	Now you need to use three Reverie APIs to activate-initiate reverie input in your application.
     a.	At first you need to validate the license. To validate license, you need to call validateKey() API in your application                 startup. This should be called only once in your application lifecycle. 
     b.	The second API is to download language specific resources for input suggestion. For this you have to call downloadResources()          API. You have to call this API while initiating language in your application. There are two possibilities of initiating                  language. One is when user selects a language from available choices and the other is when you initiate your app with user              preference. Use it in both cases. The API will download resources if it is not available otherwise it will just return with              download successful code.
     c.	The third API is to initiate the keypad. The keypad requires Activity reference to identify edit fields. So, use initKeypad()          API in every Activity wherever user input is required. For example, If you have one login and one signup activity, you have to         call this API from both these Activities.

All the three APIs are explained below.

Validate License Key:

Key validation ensures genuineness of vendor who uses our SDK. This should be a single call at startup of your application. For key validation use validateKey() API. Ideally it should be called either from your launcher Activity or Application class.

Package : com.reverie.manager
Class : RevSDK
public static void validateKey(Context context, String apiBaseUrl, String key, String pkgId, ValidationCompleteListener validationCompleteListener)

context – application context
apiBaseUrl – Reverie license server gateway. It is used whenever online validation is required. You can find this URL in sdk-info.txt.
key – your license key. You can find this URL in sdk-info.txt.
pkgId – Your registered application id.
validationCompleteListener – A public interface for callback when validation is complete.


All parameters mentioned above are compulsory. apiBaseUrl, key and pkgId will be provided once you register with Reverie for SDK. You have to implement ValidationCompleteListener in you code to get validation callback.

Package : com.reverie.manager
public interface ValidationCompleteListener {
    public void onValidationComplete(int statusCode, String statusMessage);
}

statusCode – status of the license
statusMessage – relevant message against statusCode.


You should use other Reverie APIs only if the status code is ‘1’. You can find list of status codes and relevant messages in APPENDIX.
Code Snippet:

RevSDK.validateKey(SdkTestLM.this.getApplicationContext(), TestConstants.LM_BASE_API_URL, key, pkg, 
		new ValidationCompleteListener() {
    	@Override
    	public void onValidationComplete(int statusCode, String statusMessage) {

		if(statusCode == LM.REV_LM_VALID_API_KEY)
			// Go ahead with Reverie API
		else
			//Go with default
    	}
});



Resource Download:

 The SDK is bundled with limited resources to keep it smaller in size. Resources which are required across the SDK are bundled. Some resources like dictionaries for suggestion and fonts are language dependent which again depends on user’s choice. These resources should be downloaded upon language selection by a user.  Ideally downloadResources()should be called when user selects a language or you initiate language from user preference in your application.

Package : com.reverie.manager
Class : RevSDK
 public static void downloadResources(Activity act, String resourceDownloadBaseApiUrl, int lang, 
				DownloadCompleteListener listener)

act – the calling Activity. Make sure this Activity should persist till the download complete callback is invoked.
resourceDownloadBaseApiUrl – Reverie resource server gateway. This url will be used to download resources from server. You can find this url in sdk-info.txt.
lang – Selected language code. Language code can be found in RevLangConstants class.
Listener - A public interface for callback when download is complete.


resourceDownloadBaseUrl will be provided once you register with Reverie for SDK. You have to implement DownloadCompleteListener to get download complete callback.

Package : com.reverie.manager
public interface DownloadCompleteListener {
   public void onDownloadComplete(int langCode, boolean font, boolean dict, RevError errorMsg);
}

langCode – language code sent via downloadResources API.
Font – font download status. For some languages font download may be false. Still it will work.
Dict – dictionary download status for the specific language. It should be true to work suggestion process properly.
errorMsg – RevError specifies the success or fail status of the overall download process. You should check erroMsg.getErrorCode()== RevError.NO_ERROR before going forward. If the error code is other than NO_ERROR, you should not use keypad API.


Code Snippet:
RevSDK.downloadResources(SdkTestDownloadResource.this, 
		TestConstants.RESOURCE_DOWNLOAD_BASE_API_URL, selectedLangId, new 
		DownloadCompleteListener() {
        	@Override
        	public void onDownloadComplete(int langCode, boolean font, boolean 
						dict, RevError errorMsg) {

           		 if(errorMsg.getErrorCode() == RevError.NO_ERROR)
                			//Go ahead with Reverie API
           		 else
                			// Go with default
       		 }
});




Initializing Keypad:

To act on the EditText on a screen the SDK requires Activity reference. So use initKeypad()API in each and every Activity in your application wherever Indic input is required. The SDK will take care of the remaining actions required for text input.  Make sure the call should happen after setting your contentview to the Activity. 

Package : com.reverie.manager
Class : RevSDK
public static boolean initKeypad(Activity act, int lang)

act – the calling Activity
lang – selected language code.



APPENDIX:



3.1.	Languages
Static Language constants can be found in ‘com.reverie.commom.RevLangConstants.java’
Below are the values:
Language	     Value	Access
English	     20	     RevLangConstants.Lang_English
Hindi	     0	     RevLangConstants.Lang_Hindi
Gujarati	     1	     RevLangConstants.Lang_Gujarati
Punjabi	     2	     RevLangConstants.Lang_Punjabi
Malayalam	     3	     RevLangConstants.Lang_Malayalam
Tamil	     4	     RevLangConstants.Lang_Tamil
Telugu	     5	     RevLangConstants.Lang_Telugu
Kannada	     6	     RevLangConstants.Lang_Kannada
Oriya	     7	     RevLangConstants.Lang_Oriya
Bengali	     8	     RevLangConstants.Lang_Bengali
Assamese	     9	     RevLangConstants.Lang_Assamese
Marathi	     10	     RevLangConstants.Lang_Marathi

3.2.	Domain
          Domain values are always optional. For better result in localization and transliteration you can pick up the specific domain value suitable for you.
Localization-----------
Domain name	Value
General	     1
Travel	     2
E-comerce	     3
Entertainment	4
Banking	     5
Grocery	     6
Education/Jobs	7
Medical	     8
A2P messages	9

Transliteration-------
Domain Name	Value
General	     1
Entertainment	3
Names	     19
English literals	4
Banking	     6
Brands	     7
Addresses	     9
E-commerce	12

3.3.	License Validation Status
Validation codes are defined in com.reverie.lm.LM.java. Followings are the validation codes.

Validation code	     Value	Message
LC_NT_ERROR	          4	     If network call fails to get response
LC_NT_UNAVAILABLE	     3	     If data connection not available
REV_LM_VALID_API_KEY	1	     If API key is valid
REV_LM_INVALID_API_KEY	-1	     If API key is invalid

Apart from these predefined error Codes any error response from server is also sent to the onValidationComplete() callback.

3.4.	Resource Download complete status
In onDownloadComplete callback, the RevError brings two values, one is error code and the other is error message. Following error codes are defined in com.reverie.manager.RevError.java

Error code	          Value	Message
NO_ERROR	               0	     OK/Resource already available
SERVER_RESPONSE_ERROR	1	     If server returns other than 200 response code
NETWORK_ERROR	          2	     Error description
JSON_ERROR	          3	     Error Description
NETWORK_UNAVAILABLE_ERROR	4	Data connection unavailable.
LICENSE_ERROR	          5	     Error description
LANG_INVALID_ERROR	     6	     Invalid Language


