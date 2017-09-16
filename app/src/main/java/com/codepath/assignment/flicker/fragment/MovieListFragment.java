package com.codepath.assignment.flicker.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.assignment.flicker.R;
import com.codepath.assignment.flicker.model.MovieData;
import com.codepath.assignment.flicker.util.NetworkUtil;

import org.json.JSONObject;

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

    public MovieListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNetworkUtil = NetworkUtil.getInstance();
        mMovieData = new MovieData();
        mNetworkUtil.getCurrentlyPlayingMovies((AppCompatActivity) getActivity(),
                new NetworkUtil.OnResponseListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Log.d("RESPONSE",jsonObject.toString());
                mMovieData.getMoviesList(jsonObject);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie_list,container, false);
        mUnbinder = ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
