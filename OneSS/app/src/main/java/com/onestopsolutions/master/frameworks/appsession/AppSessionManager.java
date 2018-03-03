package com.onestopsolutions.master.frameworks.appsession;

import android.content.Context;

import com.onestopsolutions.master.bean.LoginResponse;
import com.onestopsolutions.master.frameworks.dbhandler.PrefManager;
import com.onestopsolutions.master.frameworks.retrofit.GsonFactory;

/**
 * Created by Darpan on 17-Feb-18.
 */

public class AppSessionManager {

    private static final String KEY_SESSION_ID = "SESSIONID";
    Context mContext;

    public AppSessionManager(Context mContext) {
        this.mContext = mContext;
    }

    public void saveSession(LoginResponse sessionId) {
        PrefManager mPrefManager;
        mPrefManager = PrefManager.getInstance(mContext);
        mPrefManager.putString(KEY_SESSION_ID, GsonFactory.getGson().toJson(sessionId));

    }

    public LoginResponse getSession() {
        PrefManager mPrefManager;
        mPrefManager = PrefManager.getInstance(mContext);
        return GsonFactory.getGson().fromJson(mPrefManager.getString(KEY_SESSION_ID, null), LoginResponse.class);
    }

    public void clearSession() {
        PrefManager mPrefManager;
        mPrefManager = PrefManager.getInstance(mContext);
        mPrefManager.putString(KEY_SESSION_ID, null);

    }

    public boolean isRunningSession() {
        if (getSession() == null) {
            return false;
        }
        return true;
    }

}
