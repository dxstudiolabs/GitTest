package com.onestopsolutions.master.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.onestopsolutions.master.R;
import com.onestopsolutions.master.bean.Order;
import com.onestopsolutions.master.frameworks.IToolBarNavigation;
import com.onestopsolutions.master.frameworks.retrofit.ResponseResolver;
import com.onestopsolutions.master.frameworks.retrofit.RestError;
import com.onestopsolutions.master.frameworks.retrofit.WebServicesWrapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class UserDetails extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mUserId;
    private String mParam2;

    private final String orderStatus[] = {"Pending", "Complete", "Canceled"};
    private ProgressBar mProgressView;
    private IToolBarNavigation mToolbarNav;
    private ArrayList<Order> mOrderList;
    private OrderDetailsAdapter mAdapter;

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
        RecyclerView rootView = view.findViewById(R.id.order_details_container);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rootView.setLayoutManager(layoutManager);
        mAdapter = new OrderDetailsAdapter();
        rootView.setAdapter(mAdapter);
        rootView.setItemAnimator(new DefaultItemAnimator());
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
        loadUserInfo();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void loadUserInfo() {
        showProgress(true);
        WebServicesWrapper.getInstance().getOrdersForUser(mUserId, new ResponseResolver<List<Order>>() {
            @Override
            public void onSuccess(List<Order> orders, Response response) {
                mOrderList = new ArrayList<>(orders);
                mAdapter.notifyDataSetChanged();
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


    class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsViewHolder> {

        @Override
        public OrderDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_order_details, parent, false);
            return new OrderDetailsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(OrderDetailsViewHolder holder, int position) {
            Order order = mOrderList.get(position);
            holder.mUserName.setText(order.getUserID());
            holder.mLastModified.setText(order.getOrderDate());
            holder.mBookName.setText(order.getBookName());
            holder.mBookType.setText(order.getBookType());
            holder.mOrderId.setText(order.getOrderID());
            holder.mOrderStatus.setText(orderStatus[order.getOrderStatus()]);
        }

        @Override
        public int getItemCount() {
            if (mOrderList != null) return mOrderList.size();
            return 0;
        }
    }

    private class OrderDetailsViewHolder extends RecyclerView.ViewHolder {
        private TextView mUserName, mLastModified, mBookName, mBookType, mOrderId, mOrderStatus;

        public OrderDetailsViewHolder(View itemView) {
            super(itemView);
            mUserName = itemView.findViewById(R.id.card_user_name);
            mLastModified = itemView.findViewById(R.id.card_date_modified);
            mBookName = itemView.findViewById(R.id.card_book_name);
            mBookType = itemView.findViewById(R.id.card_book_type);
            mOrderId = itemView.findViewById(R.id.card_order_id);
            mOrderStatus = itemView.findViewById(R.id.card_order_status);
        }
    }

}
