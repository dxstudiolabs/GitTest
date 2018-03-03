package com.onestopsolutions.master.frameworks.appsession;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.onestopsolutions.master.LoginActivity;
import com.onestopsolutions.master.bean.LoginResponse;
import com.onestopsolutions.master.bean.User;

/**
 * Created by Darpan on 17-Feb-18.
 */

public class AppBaseApplication extends Application {
    private static AppBaseApplication mApplication;
    private User mUser = null;
    AppUserManager mAppUserManager = null;
    AppSessionManager mAppSessionManager = null;
    private LoginResponse mLoginResponse;
    public static String TAG = AppBaseApplication.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        //  JodaTimeAndroid.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        //MultiDex.install(base);
        super.attachBaseContext(base);
        mAppUserManager = new AppUserManager(this);
        mAppSessionManager = new AppSessionManager(this);
        initSession();
    }

    public static AppBaseApplication getApplication() {
        return mApplication;
    }

    public void initSession() {
        if (mAppSessionManager.isRunningSession()) {
            mLoginResponse = mAppSessionManager.getSession();
        }
    }

    public LoginResponse getSession() {
        return mAppSessionManager.getSession();
    }

    public void saveUser(User user) {
        mAppUserManager.saveUser(user);
        mUser = user;
    }

    public User getUser() {
        if (mUser == null) {
            mUser = mAppUserManager.getUser();
        }
        return mUser;
    }

    public void setSession(LoginResponse loginResponse) {
        mAppSessionManager.saveSession(loginResponse);
        mLoginResponse = loginResponse;
    }

    public boolean isUserLogin() {
        if (mLoginResponse == null) {
            return false;
        }
        return true;
    }

    public void onLogout() {
        clearSession();
        clearUser();
        startLogin();
    }

    public void startLogin() {
        Intent i = new Intent(mApplication.getBaseContext(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mApplication.getBaseContext().startActivity(i);
    }

    private void clearUser() {
        mAppUserManager.clearUser();
        mUser = null;
    }

    private void clearSession() {
        mAppSessionManager.clearSession();
        mLoginResponse = null;
    }

}
