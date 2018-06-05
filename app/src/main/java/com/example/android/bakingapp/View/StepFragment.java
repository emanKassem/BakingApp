package com.example.android.bakingapp.View;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Step;
import com.google.android.exoplayer2.C;
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

public class StepFragment extends Fragment{

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
    @BindView(R.id.thumbnailImageView)
    ImageView thumbnailImageView;
    @BindView(R.id.videoFrame)
    FrameLayout videoFrame;
    private long playbackPosition, position = C.TIME_UNSET;
    public static final String STATE = "videoState";
    private int currentWindow;
    private boolean playWhenReady = true;
    String video, thumbnail, description;
    int currentId;
    Step step;
    ArrayList<Step> steps;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, view);
        if(savedInstanceState != null){
            position = savedInstanceState.getLong(STATE, C.TIME_UNSET);
        }
        step = getArguments().getParcelable("Step");
        steps = getArguments().getParcelableArrayList("Steps");
        nextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentId < (steps.size() - 1)) {
                    step = steps.get(currentId + 1);
                    init();
                }
            }
        });
        previousImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentId > 0) {
                    step = steps.get(currentId - 1);
                    init();
                }
            }
        });
        init();
        return view;
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
        if(video.isEmpty()){
            if(!(thumbnail.isEmpty() || thumbnail.contains(".mp4"))){
                try {
                    Glide.with(getActivity()).load(thumbnail)
                            .thumbnail(0.5f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(thumbnailImageView);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                videoFrame.setVisibility(View.GONE);
            }
        } else {
            videoFrame.setVisibility(View.VISIBLE);
            initializePlayer(video);
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
            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getActivity()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            playerView.setPlayer(player);
            player.setPlayWhenReady(playWhenReady);
            if(position == C.TIME_UNSET) {
                player.seekTo(currentWindow, playbackPosition);
            }else {
                player.seekTo(currentWindow, position);
            }
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(player != null)
            position = player.getCurrentPosition();
        outState.putLong(STATE, position);

    }
}
