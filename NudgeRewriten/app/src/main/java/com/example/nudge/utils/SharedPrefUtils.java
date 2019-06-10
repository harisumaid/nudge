package com.example.nudge.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.nudge.R;

public class SharedPrefUtils {


    SharedPreferences sharedPreferences;
    Context context;

    public SharedPrefUtils(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.login_preference),context.MODE_PRIVATE);
    }
    public void writeLoginStatus(boolean Status)
    {
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean(context.getResources().getString(R.string.login_status),Status);
        editor.commit();
    }
    public boolean readLoginStatus()
    {
        boolean status;
        status=sharedPreferences.getBoolean(context.getResources().getString(R.string.login_status),false);
        return status;
    }

    public void writeAgentId(String id) {
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("agentId",id);
        editor.commit();
    }

    public String readAgentId() {
        String id;
        id=sharedPreferences.getString("agentId","");
        return id;
    }

}
