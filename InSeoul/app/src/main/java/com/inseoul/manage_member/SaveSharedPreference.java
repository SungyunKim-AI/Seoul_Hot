package com.inseoul.manage_member;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {

    static final String PREF_USER_ID = "userID";
    static final String FLAG = "signIn_FLAG";

    static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    // 계정 정보 저장
    public static void setUserID(Context context, String userID, boolean flag) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_ID, userID);
        editor.putBoolean(FLAG, flag);
        editor.commit();
    }

    // 저장된 정보 가져오기
    public static String getUserID(Context context) {
        return getSharedPreferences(context).getString(PREF_USER_ID, "");
    }

    // 자동로그인이 설정 되어있는지 확인
    public static Boolean getAutoFlag(Context context){
        return getSharedPreferences(context).getBoolean(FLAG,false);
    }

    // 로그아웃
    public static void clearUserID(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }
}
