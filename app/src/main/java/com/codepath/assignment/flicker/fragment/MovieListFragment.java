package com.codepath.assignment.flicker.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.assignment.flicker.R;
import com.codepath.assignment.flicker.adapter.MovieListAdapter;
import com.codepath.assignment.flicker.model.MovieData;
import com.codepath.assignment.flicker.util.NetworkUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment {


    Unbinder mUnbinder;
    public NetworkUtil mNetworkUtil;

    @BindView(R.id.moviesList)
    RecyclerView mRecyclerView;

    private MovieData mMovieData;
    private MovieListAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<MovieData> mMoviesList = new ArrayList<>();

    public MovieListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mNetworkUtil = NetworkUtil.getInstance();
        mMovieData = new MovieData();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie_list,container, false);
        mUnbinder = ButterKnife.bind(this,v);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new MovieListAdapter(getActivity(),mMoviesList);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
         if(savedInstanceState == null)
            updateAdapter();
        return v;
    }


    private void updateAdapter(){
        mNetworkUtil.getCurrentlyPlayingMovies((AppCompatActivity) getActivity(),
                new NetworkUtil.OnResponseListener() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        mMoviesList = mMovieData.getMoviesList(jsonObject);
                        Log.d("RESPONSE","updating the adapter");
                        mAdapter.addAllItems(mMoviesList);
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("RESPONSE","Destroy view has been called");
        mUnbinder.unbind();
        mNetworkUtil.cancelMovieCall();
    }
}
