package com.sid.videoplayground;

import android.animation.Animator;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewAnimationUtils;
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
    @BindView(R.id.revealLayout)
    View revealLayout;
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

        int mWidth = this.getResources().getDisplayMetrics().widthPixels / 2;
        int mHeight = this.getResources().getDisplayMetrics().heightPixels / 2;

        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            try {
                anim = ViewAnimationUtils.createCircularReveal(revealLayout, mWidth, mHeight, 0, MAX_WIDTH);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        revealLayout.setVisibility(View.VISIBLE);
        if (anim != null) {
            anim.start();
        }

        revealLayout.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams layoutParams = parentLayout.getLayoutParams();
        layoutParams.height = MIN_WIDTH;
        layoutParams.width = MIN_WIDTH;
        parentLayout.setLayoutParams(layoutParams);
        parentLayout.setRadius(MIN_WIDTH / 2);
    }

    public void fullScreenFrameLayout() {

        int mWidth = this.getResources().getDisplayMetrics().widthPixels / 2;
        int mHeight = this.getResources().getDisplayMetrics().heightPixels / 2;

        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            try {
                anim = ViewAnimationUtils.createCircularReveal(revealLayout, mWidth, mHeight, MAX_WIDTH, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (anim != null) {
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    revealLayout.setVisibility(View.INVISIBLE);
                    ViewGroup.LayoutParams layoutParams = parentLayout.getLayoutParams();
                    layoutParams.width = MAX_WIDTH;
                    layoutParams.height = MAX_HEIGHT;
                    parentLayout.setLayoutParams(layoutParams);
                    parentLayout.setRadius(0);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            anim.start();
        } else {
            revealLayout.setVisibility(View.INVISIBLE);
            ViewGroup.LayoutParams layoutParams = parentLayout.getLayoutParams();
            layoutParams.width = MAX_WIDTH;
            layoutParams.height = MAX_HEIGHT;
            parentLayout.setLayoutParams(layoutParams);
            parentLayout.setRadius(0);
        }
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

