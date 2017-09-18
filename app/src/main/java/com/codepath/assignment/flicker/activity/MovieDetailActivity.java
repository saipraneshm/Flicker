package com.codepath.assignment.flicker.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codepath.assignment.flicker.activity.abs.SingleFragmentActivity;
import com.codepath.assignment.flicker.fragment.MovieDetailFragment;
import com.codepath.assignment.flicker.model.MovieData;

public class MovieDetailActivity extends SingleFragmentActivity {

    private static final String EXTRA_MOVIE_DATA = "extraMovieData";
    public static Intent getNewIntent(Context context,MovieData movieData){
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE_DATA,movieData);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        MovieData movieData = getIntent().getParcelableExtra(EXTRA_MOVIE_DATA);
        return MovieDetailFragment.newInstance(movieData);
    }
}
