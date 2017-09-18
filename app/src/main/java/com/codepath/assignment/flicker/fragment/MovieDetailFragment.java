package com.codepath.assignment.flicker.fragment;


import java.text.DecimalFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.assignment.flicker.R;
import com.codepath.assignment.flicker.activity.YoutubePlayerActivity;
import com.codepath.assignment.flicker.model.MovieData;
import com.codepath.assignment.flicker.model.TrailerHelper;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_MOVIE_DATA = "movieData";
    private MovieData mMovieData;
    private Unbinder mUnbinder;

    @BindView(R.id.movie_title_text_view)
    TextView mMovieTitleTextView;

    @BindView(R.id.movie_desc_text_view)
    TextView mMovieDescTextView;

    @BindView(R.id.ratings_bar)
    AppCompatRatingBar mRatingsBar;

    @BindView(R.id.movie_image_view)
    ImageView mPosterImage;

    @BindView(R.id.overlay_play_icon_image_view)
    ImageView playIconImageView;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MovieDetailFragment newInstance(MovieData movieData) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE_DATA, movieData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mMovieData = getArguments().getParcelable(ARG_MOVIE_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mUnbinder = ButterKnife.bind(this,v);
        if(mMovieData != null){

            mMovieTitleTextView.setText(mMovieData.getTitle());
            mMovieDescTextView.setText(mMovieData.getOverview());
            mRatingsBar.setRating(getDoubleFromFloat(mMovieData.getVoteAverage()));
            Picasso.with(getActivity())
                    .load(mMovieData.getBackdropImageUrl(MovieData.BACKDROP_SIZES.W1280))
                    .resize(0,1280)
                    .into(mPosterImage);

            mPosterImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(YoutubePlayerActivity.newIntent(getActivity(),
                            TrailerHelper.getInstance()
                                    .getFirstAvailableTrailerInfo
                                            (String.valueOf(mMovieData.getId())).getKey()));
                }
            });

            /*if(TrailerHelper.getInstance().getTrailerInfoList(String.valueOf(mMovieData.getId())) == null){
                playIconImageView.setVisibility(View.INVISIBLE);
                mPosterImage.setOnClickListener(null);
            }*/
        }

        // Inflate the layout for this fragment
        return v;
    }

    private float getDoubleFromFloat(Double d){
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return Float.parseFloat(decimalFormat.format(d%4.0));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
