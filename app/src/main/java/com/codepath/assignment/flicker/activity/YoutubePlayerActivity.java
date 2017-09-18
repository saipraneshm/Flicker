package com.codepath.assignment.flicker.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codepath.assignment.flicker.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

public class YoutubePlayerActivity extends AppCompatActivity {

    private static final String EXTRA_YOUTUBE_ID = "extraYoutubeId";

    public static Intent newIntent(Context context, String youtubeId){
        Intent intent = new Intent(context, YoutubePlayerActivity.class);
        intent.putExtra(EXTRA_YOUTUBE_ID,youtubeId);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_youtube_media_player);
        final String youtubeId = getIntent().getStringExtra(EXTRA_YOUTUBE_ID);

        YouTubePlayerFragment youTubePlayerFragment = (YouTubePlayerFragment)
                getFragmentManager().findFragmentById(R.id.youtubeFragment);

        if(youTubePlayerFragment != null)
            youTubePlayerFragment.initialize(getString(R.string.youtube_api),
                    new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                            YouTubePlayer youTubePlayer, boolean b) {
                            if(youtubeId != null)
                                youTubePlayer.loadVideo(youtubeId);
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                            YouTubeInitializationResult
                                                                    youTubeInitializationResult) {

                        }
                    });
    }
}
