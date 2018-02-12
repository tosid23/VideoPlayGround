package com.sid.videoplayground;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    final int MIN_WIDTH = 550;
    final int TRANSFORMATION_START_AT = 7000;
    final int TRANSFORMATION_END_AT = 14000;
    final int ANIMATION_DURATION = 300;
    @BindView(R.id.videoPlayer)
    FrameLayout videoPlayer;
    @BindView(R.id.parentLayout)
    CardView parentLayout;
    @BindView(R.id.backgroundCard)
    CardView backgroundCard;
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

        Random r = new Random();
        ViewGroup.LayoutParams layoutParams = backgroundCard.getLayoutParams();
        layoutParams.height = r.nextInt(dpToPx(200)) + dpToPx(300);
        Log.e(TAG, "H: " + layoutParams.height);
        backgroundCard.setLayoutParams(layoutParams);

        backgroundCard.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ABOVE, R.id.backgroundCard);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.bottomMargin = -300;
        parentLayout.setLayoutParams(params);

        ValueAnimator anim = ValueAnimator.ofInt(MAX_WIDTH, MIN_WIDTH);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                try {
                    ViewGroup.LayoutParams layoutParams = parentLayout.getLayoutParams();
                    layoutParams.height = val;
                    layoutParams.width = val;
                    parentLayout.setLayoutParams(layoutParams);
                    parentLayout.setRadius(val / 2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        anim.setDuration(ANIMATION_DURATION);
        anim.start();
    }

    public void fullScreenFrameLayout() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            params.removeRule(RelativeLayout.ABOVE);
        }
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        params.bottomMargin = 0;
        parentLayout.setLayoutParams(params);

        ValueAnimator anim2 = ValueAnimator.ofInt(MIN_WIDTH, MAX_WIDTH);
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                try {
                    ViewGroup.LayoutParams layoutParams = parentLayout.getLayoutParams();
                    layoutParams.width = val;
                    layoutParams.height = val;
                    if (val >= MAX_WIDTH) {
                        parentLayout.setRadius(0);
                        layoutParams.width = val;
                        layoutParams.height = MAX_HEIGHT;
                        backgroundCard.setVisibility(View.INVISIBLE);
                    } else {
                        parentLayout.setRadius(val / 2);
                    }
                    parentLayout.setLayoutParams(layoutParams);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        anim2.setDuration(ANIMATION_DURATION);
        anim2.start();
    }

    public int dpToPx(int dps) {
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps, r.getDisplayMetrics());
        return px;
    }

    private class FirstEvent extends AsyncTask<YouTubePlayer, Void, Void> {
        @Override
        protected Void doInBackground(YouTubePlayer... youTubePlayer) {
            try {
                while (true) {
                    if (youTubePlayer[0].getCurrentTimeMillis() > TRANSFORMATION_START_AT) {
                        return null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
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
            try {
                while (true) {
                    if (youTubePlayer[0].getCurrentTimeMillis() > TRANSFORMATION_END_AT) {
                        return null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            fullScreenFrameLayout();
        }
    }
}

