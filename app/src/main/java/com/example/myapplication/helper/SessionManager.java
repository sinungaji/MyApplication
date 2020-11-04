package com.example.myapplication.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.example.myapplication.Login;

import java.util.HashMap;

public class SessionManager {
    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    private Context _context;

    private static final String PREF_NAME = "sinung";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_ID_ANGGOTA = "name";

    public static final String KEY_ABSEN_ANGGOTA = "0";


    // Constructor
    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context){
        this._context = context;
        int PRIVATE_MODE = 0;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String id_anggota, String absen){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_ID_ANGGOTA, id_anggota);

        editor.putString(KEY_ABSEN_ANGGOTA, absen);

        editor.commit();
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }

    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_ID_ANGGOTA, pref.getString(KEY_ID_ANGGOTA, null));
        user.put(KEY_ABSEN_ANGGOTA, pref.getString(KEY_ABSEN_ANGGOTA, null));
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        _context.startActivity(i);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
