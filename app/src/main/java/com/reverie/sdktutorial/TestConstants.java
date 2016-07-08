package com.reverie.sdktutorial;

import java.util.ArrayList;

/**
 * Created by ranjan_mac on 10/05/16.
 */
public class TestConstants {

    public static final String SDK_TEST_APP_ID = "com.sdktest";
    public static final String SDK_TEST_API_KEY = "3ZsjdNhiZcXXs3tP4OHjzJAPXJM0yojqxzyi";


    public static final String LM_API_BASE_URL = "http://137.135.43.145:80";
    public static final String LOCAL_TRANS_BASE_URL = "http://api.revup.reverieinc.com/v2";

    public static final int Server_Lang_Hindi=0;
    public static final int Server_Lang_Gujarati=1;
    public static final int Server_Lang_Punjabi=2;
    public static final int Server_Lang_Malayalam=3;
    public static final int Server_Lang_Tamil=4;
    public static final int Server_Lang_Telugu=5;
    public static final int Server_Lang_Kannada=6;
    public static final int Server_Lang_Odia=7;
    public static final int Server_Lang_Bengali=8;
    public static final int Server_Lang_Assamese=9;
    public static final int Server_Lang_Marathi=10;
    public static final int Server_Lang_Nepali=11;
    public static final int Server_Lang_Bodo=12;
    public static final int Server_Lang_Dogri=13;
    public static final int Server_Lang_Konkani=14;
    public static final int Server_Lang_Kashmiri=15;
    public static final int Server_Lang_Maithili=16;
    public static final int Server_Lang_Manipuri=17;
    public static final int Server_Lang_Sanskrit=18;
    public static final int Server_Lang_Sindhi=19;
    public static final int Server_Lang_English=20;

    public static final String[] langNamesArray =
            {"Hindi", "Gujrati", "Punjabi", "Malayalam", "Tamil", "Telugu", "Kannad",
                "Odia", "Bengali", "Assamese", "Marathi", "Nepali", "Bodo", "Dogri",
                "Konkani", "kashmiri", "Maithili", "Manipuri","Sanskrit","Sindhi","English"
            };

    public static final int[] langIdsArray = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};

    public static ArrayList<Lang> getLanguageList() {
        ArrayList<Lang> languages = new ArrayList<>();
        for(int i = 0; i <= 20; i++) {
            languages.add(new Lang(i, langNamesArray[i]));
        }

        return languages;
    }

}
