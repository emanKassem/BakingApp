package com.example.android.bakingapp.View;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity {

    private SimpleExoPlayer player;
    @BindView(R.id.video_view)
    SimpleExoPlayerView playerView;
    @BindView(R.id.step_description)
    TextView stepDescription;
    @BindView(R.id.nextImageButton)
    ImageButton nextImageButton;
    @BindView(R.id.previousImageButton)
    ImageButton previousImageButton;
    @BindView(R.id.stepNumberTextView)
    TextView stepNumberTextView;
    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;
    String video, thumbnail, description;
    int currentId;
    Step step;
    ArrayList<Step> steps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        step = bundle.getParcelable("Step");
        steps = bundle.getParcelableArrayList("Steps");
        init();
        }

    private void init() {
        description = step.getDescription();
        video = step.getVideoURL();
        thumbnail = step.getThumbnailURL();
        stepDescription.setText(description);
        currentId = step.getId();
        stepNumberTextView.setText("Step "+currentId);
        checkInitializePlayer();

    }
    void checkInitializePlayer(){
        if(video.isEmpty() && thumbnail.isEmpty()){
            playerView.setVisibility(View.GONE);
        }else if(thumbnail.isEmpty()){
            initializePlayer(video);
        }else {
            initializePlayer(thumbnail);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            checkInitializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            checkInitializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void initializePlayer(String url) {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            playerView.setPlayer(player);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
        }
        MediaSource mediaSource = buildMediaSource(Uri.parse(url));
        player.prepare(mediaSource, true, false);
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory("exoplayer-codelab"))
                .createMediaSource(uri);
    }

    public void onClick(View view) {
        if(view.getId() == R.id.previousImageButton){
            {
                if(currentId>0){
                    step = steps.get(currentId-1);
                    init();
                }
            }

        }else if(view.getId() == R.id.nextImageButton){
            if(currentId <(steps.size()-1)){
                step = steps.get(currentId+1);
                init();
            }

        }
    }
}
