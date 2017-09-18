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
import com.codepath.assignment.flicker.model.TrailerData;
import com.codepath.assignment.flicker.model.TrailerHelper;
import com.codepath.assignment.flicker.util.NetworkUtil;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import okhttp3.OkHttpClient;

/**
 * Created by saip92 on 9/16/2017.
 */

public class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MovieData> mMoviesList;
    private OkHttpClient client;
    private Picasso mPicasso;
    private Context mContext;
    private onItemClickListener mListener;

    private final int MOVIE = 0, POPULAR_MOVIE = 1;

    public interface onItemClickListener{
        void onMovieClick(MovieData movieData);
        void onPopularMovieClick(MovieData movieData);
    }

    public MovieListAdapter(Context context, List<MovieData> moviesList){
        mContext = context;
        mMoviesList = moviesList;
        client = NetworkUtil.getInstance().getHttpClient();
        mPicasso = new Picasso.Builder(mContext).downloader(new OkHttp3Downloader(client)).build();
    }

    public void setListener(onItemClickListener listener) {
        mListener = listener;
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onMovieClick(mMoviesList.get(getAdapterPosition()));
                }
            });
        }

        void bindMovie(MovieData movieData){
            if(movieData != null){
                titleTextView.setText(movieData.getTitle());
                movieDescTextView.setText(movieData.getOverview());
                mPicasso.load(movieData.getPosterImageUrl(MovieData.POSTER_SIZES.W780))
                        .transform(new RoundedCornersTransformation(10,10))
                        .placeholder(R.drawable.placeholder_150x280)
                        .resize(0,780)
                        .into(posterImageView);
            }
        }

    }

    class PopularMovieViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.movie_image_view)
        ImageView mImageView;

        @BindView(R.id.movie_title_text_view)
        @Nullable
        TextView mMovieTitle;

        @BindView(R.id.movie_desc_text_view)
        @Nullable
        TextView mMovieDesc;

        @BindView(R.id.overlay_play_icon_image_view)
        ImageView playIconImageView;

        PopularMovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onPopularMovieClick(mMoviesList.get(getAdapterPosition()));
                }
            });
        }

        void bindPopularMovie(MovieData movieData){
            if(movieData != null){
                /*if(TrailerHelper.getInstance().getTrailerInfoList(String.valueOf(movieData.getId())) == null){
                    playIconImageView.setVisibility(View.INVISIBLE);
                    itemView.setOnClickListener(null);
                }*/
                if(itemView.findViewById(R.id.movie_title_text_view) != null){
                    mMovieTitle.setText(movieData.getTitle());
                    mMovieDesc.setText(movieData.getOverview());
                    mPicasso.load(movieData.getBackdropImageUrl(MovieData.BACKDROP_SIZES.W1280))
                            .transform(new RoundedCornersTransformation(10,10))
                            .placeholder(R.drawable.placeholder_400x280)
                            .resize(1280,0)
                            .into(mImageView);
                }else{
                    mPicasso.load(movieData.getBackdropImageUrl(MovieData.BACKDROP_SIZES.W1280))
                            .transform(new RoundedCornersTransformation(10,10))
                            .placeholder(R.drawable.placeholder_780x500)
                            .resize(1280,0)
                            .into(mImageView);
                }
            }
        }
    }

}

