package com.inseoul.manage_member;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {

    static final String PREF_USER_ID = "userID";
    static final String PREF_USER_ID_NUM = "userID_NUM";
    static final String FLAG = "signIn_FLAG";
    static final String PREF_USER_NAME = "userName";
    static final String PREF_USER_MAIL = "userMail";

    static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    // 계정 정보 저장
    public static void setUserID(Context context, String userID, Integer userID_NUM ,boolean flag) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_ID, userID);
        editor.putInt(PREF_USER_ID_NUM,userID_NUM);
        editor.putBoolean(FLAG, flag);
        editor.commit();
    }

    public static void setUserInfo(Context context, String UserNm, String UserMail){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_NAME,UserNm);
        editor.putString(PREF_USER_MAIL,UserMail);
        editor.commit();
    }

    // 저장된 정보 가져오기
    public static String getUserID(Context context) {
        return getSharedPreferences(context).getString(PREF_USER_ID, "");
    }
    public static Integer getUserID_NUM(Context context){
        return getSharedPreferences(context).getInt(PREF_USER_ID_NUM,0);
    }
    public static String getUserName(Context context) {
        return getSharedPreferences(context).getString(PREF_USER_NAME, "");
    }
    public static String getUserMail(Context context) {
        return getSharedPreferences(context).getString(PREF_USER_MAIL, "");
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
