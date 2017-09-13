package com.codepath.assignment.flicker.activity;

import android.support.v4.app.Fragment;

import com.codepath.assignment.flicker.activity.abs.SingleFragmentActivity;
import com.codepath.assignment.flicker.fragment.MovieListFragment;

public class MovieListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new MovieListFragment();
    }

}
