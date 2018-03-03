package com.onestopsolutions.master.frameworks.appsession;

import android.content.Context;

import com.onestopsolutions.master.bean.User;
import com.onestopsolutions.master.frameworks.dbhandler.PrefManager;
import com.onestopsolutions.master.frameworks.retrofit.GsonFactory;

/**
 * Created by Darpan on 17-Feb-18.
 */

public class AppUserManager {
    private static final String KEY_SESSION_ID = "USER";
    private static final String INTENT_USER_SYNC_COMPLETED = "USER_SYNC_COMPLETED";
    private Context mContext;

    AppUserManager(Context context) {
        mContext = context;
    }

    public void saveUser(User user) {
        PrefManager mPrefManager;
        mPrefManager = PrefManager.getInstance(mContext);
        mPrefManager.putString(KEY_SESSION_ID, GsonFactory.getGson().toJson(user));

    }

    public User getUser() {
        PrefManager mPrefManager;
        mPrefManager = PrefManager.getInstance(mContext);
        return GsonFactory.getGson().fromJson(mPrefManager.getString(KEY_SESSION_ID, null), User.class);
    }

    public void clearUser() {
        PrefManager mPrefManager;
        mPrefManager = PrefManager.getInstance(mContext);
        mPrefManager.putString(KEY_SESSION_ID, null);
    }
}
