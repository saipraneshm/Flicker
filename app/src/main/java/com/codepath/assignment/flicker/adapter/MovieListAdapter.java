package com.codepath.assignment.flicker.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.assignment.flicker.R;
import com.codepath.assignment.flicker.model.MovieData;
import com.codepath.assignment.flicker.util.NetworkUtil;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;

/**
 * Created by saip92 on 9/16/2017.
 */

public class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MovieData> mMoviesList;
    private OkHttpClient client;
    private Picasso mPicasso;
    private Context mContext;

    private final int MOVIE = 0, POPULAR_MOVIE = 1;

    public MovieListAdapter(Context context, List<MovieData> moviesList){
        mContext = context;
        mMoviesList = moviesList;
        client = NetworkUtil.getInstance().getHttpClient();
        mPicasso = new Picasso.Builder(context).downloader(new OkHttp3Downloader(client)).build();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType){
            case MOVIE:
                view = inflater.inflate(R.layout.layout_movie_viewholder,parent, false);
                viewHolder = new MovieViewHolder(view);
                break;
            case POPULAR_MOVIE:
                view = inflater.inflate(R.layout.layout_popular_movie_viewholder, parent, false);
                viewHolder = new PopularMovieViewHolder(view);
                break;
            default:
                view = inflater.inflate(R.layout.layout_movie_viewholder,parent, false);
                viewHolder = new MovieViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch(getItemViewType(position)){
            case MOVIE:
                MovieViewHolder movieVH = (MovieViewHolder) holder;
                movieVH.bindMovie(mMoviesList.get(position));
                break;
            case POPULAR_MOVIE:
                PopularMovieViewHolder popularMovieVH = (PopularMovieViewHolder) holder;
                popularMovieVH.bindPopularMovie(mMoviesList.get(position));
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mMoviesList.get(position).getVoteAverage() > 5.0){
            return POPULAR_MOVIE;
        }else{
            return MOVIE;
        }
    }


    public void addAllItems(List<MovieData> movies){
        mMoviesList.clear();
        mMoviesList.addAll(movies);
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.poster_image_view)
        ImageView posterImageView;
        @BindView(R.id.movie_title_text_view)
        TextView titleTextView;
        @BindView(R.id.movie_desc_text_view)
        TextView movieDescTextView;

        MovieViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        void bindMovie(MovieData movieData){
            if(movieData != null){
                titleTextView.setText(movieData.getTitle());
                movieDescTextView.setText(movieData.getOverview());
                mPicasso.load(movieData.getPosterImageUrl(MovieData.POSTER_SIZES.W780))
                        .resize(0,780)
                        .into(posterImageView);
            }
        }

    }

    class PopularMovieViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.popular_movie_image_view)
        ImageView mImageView;


        @BindView(R.id.movie_title_text_view)
        @Nullable
        TextView mMovieTitle;

        @BindView(R.id.movie_desc_text_view)
        @Nullable
        TextView mMovieDesc;

        PopularMovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        void bindPopularMovie(MovieData movieData){
            if(movieData != null){
                mPicasso.load(movieData.getBackdropImageUrl(MovieData.BACKDROP_SIZES.W1280))
                        .resize(1280,0)
                        .into(mImageView);
                if(itemView.findViewById(R.id.movie_title_text_view) != null){
                    mMovieTitle.setText(movieData.getTitle());
                    mMovieDesc.setText(movieData.getOverview());
                }
            }
        }
    }

}

