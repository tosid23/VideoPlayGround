package com.sid.videoplayground;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    final int MIN_WIDTH = 600;
    final int TRANSFORMATION_START_AT = 7000;
    final int TRANSFORMATION_END_AT = 14000;
    final int ANIMATION_DURATION = 300;
    @BindView(R.id.videoPlayer)
    FrameLayout videoPlayer;
    @BindView(R.id.parentLayout)
    CardView parentLayout;
    YouTubePlayerSupportFragment ytfrag;
    String videoLink = "8nHBGFKLHZQ";
    String youtube_key = "AIzaSyDVEwKhJb2SNBUHVVhCSJOVOSAsgKtUtyI";
    String TAG = "MainActivity";
    Context context;
    int MAX_HEIGHT = 0;
    int MAX_WIDTH = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;

        ytfrag = YouTubePlayerSupportFragment.newInstance();

        ytfrag.initialize(youtube_key, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(videoLink);
                new FirstEvent().execute(youTubePlayer);
                new SecondEvent().execute(youTubePlayer);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.videoPlayer, ytfrag).commit();
    }

    public void roundFrameLayout() {
        MAX_HEIGHT = parentLayout.getMeasuredHeight();
        MAX_WIDTH = parentLayout.getMeasuredWidth();

        ValueAnimator anim = ValueAnimator.ofInt(MAX_WIDTH, MIN_WIDTH);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = parentLayout.getLayoutParams();
                layoutParams.height = val;
                layoutParams.width = val;
                parentLayout.setLayoutParams(layoutParams);
                parentLayout.setRadius(val / 2);
            }
        });
        anim.setDuration(ANIMATION_DURATION);
        anim.start();
    }

    public void fullScreenFrameLayout() {
        ValueAnimator anim2 = ValueAnimator.ofInt(MIN_WIDTH, MAX_WIDTH);
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = parentLayout.getLayoutParams();
                layoutParams.width = val;
                layoutParams.height = val;
                if (val >= MAX_WIDTH) {
                    parentLayout.setRadius(0);
                    layoutParams.width = val;
                    layoutParams.height = MAX_HEIGHT;
                } else {
                    parentLayout.setRadius(val / 2);
                }
                parentLayout.setLayoutParams(layoutParams);
            }
        });
        anim2.setDuration(ANIMATION_DURATION);
        anim2.start();
    }

    private class FirstEvent extends AsyncTask<YouTubePlayer, Void, Void> {
        @Override
        protected Void doInBackground(YouTubePlayer... youTubePlayer) {
            while (true) {
                if (youTubePlayer[0].getCurrentTimeMillis() > TRANSFORMATION_START_AT) {
                    return null;
                }
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            roundFrameLayout();
        }
    }

    private class SecondEvent extends AsyncTask<YouTubePlayer, Void, Void> {
        @Override
        protected Void doInBackground(YouTubePlayer... youTubePlayer) {
            while (true) {
                if (youTubePlayer[0].getCurrentTimeMillis() > TRANSFORMATION_END_AT) {
                    return null;
                }
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            fullScreenFrameLayout();
        }
    }
}

