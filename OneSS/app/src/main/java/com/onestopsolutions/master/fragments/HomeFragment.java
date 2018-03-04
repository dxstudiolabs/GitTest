package com.onestopsolutions.master.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onestopsolutions.master.R;
import com.onestopsolutions.master.frameworks.IToolBarNavigation;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private static TabLayout mTabLayout;
    private ViewPager mViewPager;
    private OnFragmentInteractionListener mListener;
    ViewPagerAdapter mAdapter;
    private IToolBarNavigation mToolNav;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(TabLayout tabLayouttmp) {
        HomeFragment fragment = new HomeFragment();
        mTabLayout = tabLayouttmp;
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setupViewPager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_home, container, false);
        mViewPager = (ViewPager) mView.findViewById(R.id.viewpager);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setVisibility(View.VISIBLE);

        return mView;
    }

    private void setupViewPager() {
        mAdapter = new ViewPagerAdapter(getChildFragmentManager());
        mAdapter.addFragment(DataFragment.newInstance("1", "Gross Income"), "Gross Income");
        mAdapter.addFragment(DataFragment.newInstance("2", "Tax"), "Tax");
        mAdapter.addFragment(DataFragment.newInstance("3", "Net Income"), "Net Income");
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(R.string.title_home_fragment);
        mToolNav.removeBackArow();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
        if (context instanceof IToolBarNavigation) {
            mToolNav = (IToolBarNavigation) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mTabLayout.setupWithViewPager(null);
        mViewPager = null;
        mAdapter = null;
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
