package com.onestopsolutions.master.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.onestopsolutions.master.MainActivity;
import com.onestopsolutions.master.R;
import com.onestopsolutions.master.bean.Order;
import com.onestopsolutions.master.frameworks.IToolBarNavigation;
import com.onestopsolutions.master.frameworks.retrofit.ResponseResolver;
import com.onestopsolutions.master.frameworks.retrofit.RestError;
import com.onestopsolutions.master.frameworks.retrofit.WebServicesWrapper;

import java.util.List;

import retrofit2.Response;

public class UserDetails extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mUserId;
    private String mParam2;
    private TextView mUserName, mLastModified, mBookName, mBookType, mOrderId, mOrderStatus;
    private final String orderStatus[] = {"Pending", "Complete", "Canceled"};
    private ProgressBar mProgressView;
    private IToolBarNavigation mToolbarNav;

    public UserDetails() {
    }

    public static UserDetails newInstance(String mUserId) {
        UserDetails fragment = new UserDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mUserId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            return getActivity().getSupportFragmentManager().popBackStackImmediate();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IToolBarNavigation)
            mToolbarNav = (IToolBarNavigation) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        mUserName = view.findViewById(R.id.card_user_name);
        mLastModified = view.findViewById(R.id.card_date_modified);
        mBookName = view.findViewById(R.id.card_book_name);
        mBookType = view.findViewById(R.id.card_book_type);
        mOrderId = view.findViewById(R.id.card_order_id);
        mOrderStatus = view.findViewById(R.id.card_order_status);
        mProgressView = view.findViewById(R.id.loading_progress);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_PARAM1, mUserId);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("User Details");
        mToolbarNav.addBackArrow();
        showProgress(true);
        loadUserInfo();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void loadUserInfo() {
        WebServicesWrapper.getInstance().getOrdersForUser(mUserId, new ResponseResolver<List<Order>>() {
            @Override
            public void onSuccess(List<Order> orders, Response response) {
                if (orders.size() > 0) {
                    Order order = orders.get(0);
                    mUserName.setText(orders.get(0).getUserID());
                    mLastModified.setText(order.getOrderDate());
                    mBookName.setText(order.getBookName());
                    mBookType.setText(order.getBookType());
                    mOrderId.setText(order.getOrderID());
                    mOrderStatus.setText(orderStatus[order.getOrderStatus()]);
                }
                showProgress(false);
            }

            @Override
            public void onFailure(RestError error, String msg) {
                showProgress(false);
            }
        });
    }


    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
