# Reverie Android SDK API


This project demonstrates using of Reverie Android SDK API in your application.

Steps
- Import this project to your Android Studio workspace. 
- [Register](http://revup.reverieinc.com) to RevUp to get `apikey` and `appid` and download SDK Package.
- Add the library aar from the SDK Package to your project
- Update the following constants in `TestConstants.java` file.
```
public static final String SDK_TEST_APP_ID = "[your app id]";
public static final String SDK_TEST_API_KEY = "[Your API key]";

public static final String REVUP_LOCALIZATION_API_ENDPOINT = "https://api-gw.revup.reverieinc.com/apiman-gateway/[YourOrgName]/localization/1.0";
public static final String REVUP_TRANSLITERATION_API_ENDPOINT = "https://api-gw.revup.reverieinc.com/apiman-gateway/[YourOrgName]/transliteration/1.0";

Terminology:
- `SDK_TEST_APP_ID` - is the package name of your application.
- `SDK_TEST_API_KEY` - is the apikey for you once you register through RevUP.
- `REVUP_LOCALIZATION_API_ENDPOINT` - Endpoint to Reverie Localization server
- `REVUP_TRANSLITERATION_API_ENDPOINT` - Endpoint to Reverie Transliteration server.

Once these variables are updated run the project, and you are good to go.

To integrate the library to your own project, please refer the API documentation provided with the SDK package.
