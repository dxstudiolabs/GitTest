package com.onestopsolutions.master;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.onestopsolutions.master.bean.GrossIncome;
import com.onestopsolutions.master.bean.NetIncome;
import com.onestopsolutions.master.bean.Order;
import com.onestopsolutions.master.bean.Tax;
import com.onestopsolutions.master.bean.User;
import com.onestopsolutions.master.fragments.HomeFragment;
import com.onestopsolutions.master.fragments.InputDataFragment;
import com.onestopsolutions.master.fragments.UserDetails;
import com.onestopsolutions.master.fragments.UserList;
import com.onestopsolutions.master.frameworks.IToolBarNavigation;
import com.onestopsolutions.master.frameworks.appsession.AppBaseApplication;
import com.onestopsolutions.master.frameworks.retrofit.ResponseResolver;
import com.onestopsolutions.master.frameworks.retrofit.RestError;
import com.onestopsolutions.master.frameworks.retrofit.WebServicesWrapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener, UserList.onUserClickListener, IToolBarNavigation {
    // Fragment mHomeFragment = null;
    TabLayout mTabLayout = null;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setUserDetails(navigationView.getHeaderView(0));
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        Fragment mHomeFragment = HomeFragment.newInstance(mTabLayout);
        loadFragment(mHomeFragment, "HomeFragment", false);

    }

    private void setUserDetails(View navView) {
        ((TextView) navView.findViewById(R.id.name)).setText(AppBaseApplication.getApplication().getSession().getUserId());
        ((TextView) navView.findViewById(R.id.email)).setText(AppBaseApplication.getApplication().getSession().getRole());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public ActionBarDrawerToggle getActionBarToggle() {
        return toggle;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this, InputDataActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            loadFragment(HomeFragment.newInstance(mTabLayout), "HomeFragment", false);
        } else if (id == R.id.nav_account) {
        } else if (id == R.id.nav_user_details) {
            loadFragment(new UserList(), "UserList", true);
        } else if (id == R.id.nav_terms) {
        } else if (id == R.id.nav_help_feedback) {
        } else if (id == R.id.nav_about) {
        } else if (id == R.id.nav_log_out) {
            AppBaseApplication.getApplication().onLogout();
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void loadFragment(Fragment fragment, String tag, boolean addTobackstack) {
        mTabLayout.setVisibility(View.GONE);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment, tag);
        if (addTobackstack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onUserClick(String userId) {
        mTabLayout.setVisibility(View.GONE);
        Fragment userDetails = UserDetails.newInstance(userId);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, userDetails, "UserDetails");
        fragmentTransaction.addToBackStack("UserDetails");
        fragmentTransaction.commit();
    }

    @Override
    public void addBackArrow() {
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().popBackStackImmediate();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void removeBackArow() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.setToolbarNavigationClickListener(null);
    }
}
