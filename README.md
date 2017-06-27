# Reverie Android SDK API


This project demonstrates using of Reverie Android SDK in your application.

Steps
- Import this project to your Android Studio workspace. 
- [Register](http://revup.reverieinc.com) to RevUp to get `apikey` and `appid` and download SDK Package.
- Add the library aar from the SDK Package to your project
- Update the following constants in `TestConstants.java` file.
```
public static final String SDK_TEST_APP_ID = "[your app id]";
public static final String SDK_TEST_API_KEY = "[Your API key]";

public static final String LM_BASE_API_URL = "https://api-gw.revup.reverieinc.com/apiman-gateway/ReverieMobilitySdk";
public static final String RESOURCE_DOWNLOAD_BASE_API_URL = "https://api-gw.revup.reverieinc.com/apiman-gateway/ReverieMobilitySdk";

Terminology:
- `SDK_TEST_APP_ID` - is the package name of your application.
- `SDK_TEST_API_KEY` - is the apikey for you once you register through RevUP.
- `LM_BASE_API_URL` - Endpoint to Reverie license validation

Once these variables are updated run the project, and you are good to go.

To integrate the library to your own project, please refer the API documentation provided with the SDK package.
