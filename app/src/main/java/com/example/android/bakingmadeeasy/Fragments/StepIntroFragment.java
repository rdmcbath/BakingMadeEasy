package com.example.android.bakingmadeeasy.Fragments;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.bakingmadeeasy.Adapters.StepAdapter;
import com.example.android.bakingmadeeasy.Networks.Ingredient;
import com.example.android.bakingmadeeasy.Networks.Recipe;
import com.example.android.bakingmadeeasy.Networks.RecipeStep;
import com.example.android.bakingmadeeasy.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.media.session.MediaButtonReceiver;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.android.bakingmadeeasy.R.string.step;

/**
 * Created by Rebecca on 6/27/2017.
 */

public class StepIntroFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String TAG = StepIntroFragment.class.getSimpleName();
    @BindView(R.id.player_view)
    SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.thumbnail)
    ImageView thumbnail;
    @BindView(R.id.previous_button)
    Button prevButton;
    @BindView(R.id.next_button)
    Button nextButton;
    Unbinder unbinder;
    @BindView(R.id.video_container)
    LinearLayout videoContainer;
    private Recipe recipe;
    private List<RecipeStep> recipeStepList;
    public List<Ingredient> ingredientList;
    public Ingredient ingredients;
    private int position;
    private boolean mDualPane;
    private StepAdapter mStepAdapter;
    public LinearLayoutManager mLayoutManager;
    private SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private NotificationManager mNotificationManager;
    public static final String RECIPE_STEP_LIST_INDEX = "position";
    public static final String RECIPE_DETAIL_STEP_LIST = "step";

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public StepIntroFragment() {
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            recipeStepList = savedInstanceState.getParcelableArrayList(RECIPE_DETAIL_STEP_LIST);
            position = savedInstanceState.getInt(RECIPE_STEP_LIST_INDEX);
        }

        View rootView = inflater.inflate(R.layout.fragment_intro_step, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        if (recipeStepList != null) {

            simpleExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                    (this.getResources(), R.drawable.cheesecake));

            // Initialize the Media Session.
            initializeMediaSession();
            // Initialize the player.
            initializePlayer(Uri.parse(recipeStepList.get(position).getVideoURL()));

            description.setText(recipeStepList.get(position).getDescription());

            if (recipeStepList.get(position).getThumbnailURL() == "")
                Picasso.get().load(recipeStepList.get(position)
                        .getThumbnailURL()).into(thumbnail);

        } else {
            Toast.makeText(getContext(), "Nothing to see here \uD83D\uDE15", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getContext(),
                RecipeStepDetailFragment.class.getSimpleName());

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);
    }

    /**
     * Initialize ExoPlayer.
     *
     * @param videoURL The URI of the sample to play.
     */
    private void initializePlayer(Uri videoURL) {
        if (mExoPlayer == null) {

            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(videoURL, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }
    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @OnClick(R.id.next_button)
    public void nextStep() {
        if (position < recipeStepList.size() - 1)
            position++;
        else
            position = 0;

        releasePlayer();

        initializePlayer(Uri.parse(recipeStepList.get(position).getVideoURL()));

        description.setText(recipeStepList.get(position).getDescription());
        if (recipeStepList.get(position).getThumbnailURL() == "")
            Picasso.get().load(recipeStepList.get(position).getThumbnailURL()).into(thumbnail);
    }

    @OnClick(R.id.previous_button)
    public void previousStep() {
        if (position == 0)
            position = recipeStepList.size() - 1;
        else
            position--;

        releasePlayer();

        initializePlayer(Uri.parse(recipeStepList.get(position).getVideoURL()));

        description.setText(recipeStepList.get(position).getDescription());
        if (recipeStepList.get(position).getThumbnailURL() == "")
            Picasso.get().load(recipeStepList.get(position).getThumbnailURL()).into(thumbnail);
    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }
        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }
        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    public void setRecipeStepList(List<RecipeStep> recipeStepList) {
        this.recipeStepList = recipeStepList;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_DETAIL_STEP_LIST, (ArrayList<? extends Parcelable>) recipeStepList);
        outState.putInt(RECIPE_STEP_LIST_INDEX, position);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(step);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        releasePlayer();
        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
    }

    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }
}

