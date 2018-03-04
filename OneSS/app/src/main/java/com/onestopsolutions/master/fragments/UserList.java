package com.onestopsolutions.master.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.onestopsolutions.master.MainActivity;
import com.onestopsolutions.master.R;
import com.onestopsolutions.master.bean.User;
import com.onestopsolutions.master.frameworks.IToolBarNavigation;
import com.onestopsolutions.master.frameworks.retrofit.ResponseResolver;
import com.onestopsolutions.master.frameworks.retrofit.RestError;
import com.onestopsolutions.master.frameworks.retrofit.WebServices;
import com.onestopsolutions.master.frameworks.retrofit.WebServicesWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import retrofit2.Response;


public class UserList extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private onUserClickListener mListener;
    @BindView(R.id.user_list_container)
    private RecyclerView mRootView;

    private UserListAdapter mAdapter;
    private ArrayList<User> mUserList;
    private ProgressBar mProgressView;
    private IToolBarNavigation mToolBarNav;

    public UserList() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRootView = view.findViewById(R.id.user_list_container);
        mProgressView = view.findViewById(R.id.loading_progress);
        showProgress(true);
        mAdapter = new UserListAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRootView.setLayoutManager(mLayoutManager);
        mRootView.addItemDecoration(new DividerItemDecoration(mRootView.getContext(), LinearLayoutManager.VERTICAL));
        mRootView.setItemAnimator(new DefaultItemAnimator());
        mRootView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        ButterKnife.bind(UserList.this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("All Users");
        mToolBarNav.addBackArrow();
        loadUserList();
    }

    private void loadUserList() {
        WebServicesWrapper.getInstance().getUserList(new ResponseResolver<List<User>>() {
            @Override
            public void onSuccess(List<User> userList, Response response) {
                mUserList = new ArrayList<>(userList);
                mAdapter.notifyDataSetChanged();
                showProgress(false);
            }

            @Override
            public void onFailure(RestError error, String msg) {
                showProgress(false);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onUserClickListener) {
            mListener = (onUserClickListener) context;
        }
        if (context instanceof IToolBarNavigation) {
            mToolBarNav = (IToolBarNavigation) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class UserListAdapter extends RecyclerView.Adapter<UserViewHolder> {
        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_user_list, parent, false);
            return new UserViewHolder(view);
        }

        @Override
        public void onBindViewHolder(UserViewHolder holder, int position) {
            holder.userName.setText((1 + position) + ". " + mUserList.get(position).getEmail()); //TODO : change email to name
            holder.lastModified.setText(mUserList.get(position).getLastModified());
        }

        @Override
        public int getItemCount() {
            if (mUserList == null) return 0;
            return mUserList.size();
        }
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

    private class UserViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_user_name)
        TextView userName;
        @BindView(R.id.item_last_modified)
        TextView lastModified;

        public UserViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.item_user_name);
            lastModified = itemView.findViewById(R.id.item_last_modified);
            ButterKnife.bind(UserViewHolder.this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick(getAdapterPosition());
                }
            });
        }

        @OnItemClick(R.id.user_list_container)
        void onItemClick(int position) {
            if (mListener != null) {
                mListener.onUserClick(mUserList.get(position).getUserID());
            }
        }
    }

    public interface onUserClickListener {
        void onUserClick(String userId);
    }
}
