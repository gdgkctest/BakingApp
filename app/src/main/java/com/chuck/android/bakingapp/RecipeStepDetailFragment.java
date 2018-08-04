package com.chuck.android.bakingapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chuck.android.bakingapp.adapters.IngredientsAdapter;
import com.chuck.android.bakingapp.models.Ingredient;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static com.chuck.android.bakingapp.utils.MyUtils.getMimeType;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListStepActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepDetailActivity}
 * on handsets.
 */
public class RecipeStepDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_DESCRIPTION = "item_description";
    public static final String ARG_ITEM_LONG_DESCRIPTION = "item_long_description";
    public static final String ARG_ITEM_VIDEO_URL = "item_video_url";
    public static final String ARG_ITEM_THUMBNAIL_URL = "item_thumbnail_url";
    public static final String BUNDLE_VIDEOPOSITION = "video saved position";
    public static final String BUNDLE_VIDEOPLAYING = "is video playing";
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    private static final String APP_NAME = RecipeStepDetailFragment.class.getSimpleName();
    private static MediaSessionCompat mMediaSession;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private PlaybackStateCompat.Builder mStateBuilder;
    private Context context;
    private RecyclerView recyclerView;
    private IngredientsAdapter adapter;
    private List<Ingredient> mIngredients;
    private long videoPosition;
    private boolean videoPlaying;

    /**
     * The dummy content this fragment is presenting.
     */
    private Integer id;
    private String description;
    private String longDescription;
    private String videoURL;
    private boolean isIngredients;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
//    public RecipeStepDetailFragment() {
//    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null && mExoPlayer != null) {
            Long playerPosition = savedInstanceState.getLong(BUNDLE_VIDEOPOSITION);
            Boolean playerPlaying = savedInstanceState.getBoolean(BUNDLE_VIDEOPLAYING);
            mExoPlayer.seekTo(playerPosition);
            mExoPlayer.setPlayWhenReady(playerPlaying);
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getArguments() != null;
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            id = getArguments().getInt(ARG_ITEM_ID);
            //Check if Step is Ingredients
            if (id == -1)
                isIngredients = true;
            description = getArguments().getString(ARG_ITEM_DESCRIPTION);
            longDescription = getArguments().getString(ARG_ITEM_LONG_DESCRIPTION);
            videoURL = getArguments().getString(ARG_ITEM_VIDEO_URL);
        }
        Activity activity = this.getActivity();
        assert activity != null;
        if (getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
            activity.setTitle("Recipe Step Details");
            if (isIngredients)
                activity.setTitle(description);
            else
                activity.setTitle(Integer.toString(id) + " " + description);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);
        mPlayerView = rootView.findViewById(R.id.recipe_detail_video_view);
        if (longDescription != null && getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT)
            ((TextView) rootView.findViewById(R.id.recipe_detail_text)).setText(longDescription);

        if (isIngredients) {
            recyclerView = rootView.findViewById(R.id.ingredientList);
            initRecyclerView();
            adapter.setIngredients(context);
            mPlayerView.setVisibility(View.GONE);
            return rootView;
        }
        String mimeType = getMimeType(videoURL);
        if (mimeType == null) {
            mPlayerView.setVisibility(View.GONE);
            ((TextView) rootView.findViewById(R.id.recipe_detail_text)).setText(longDescription);
            return rootView;
        }
        if (mimeType.equals("video/mp4")) {
            //Hide Description Text in landsacpe
            if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE)
                ((TextView) rootView.findViewById(R.id.recipe_detail_text)).setVisibility(View.GONE);
            // 1. Create a default TrackSelector
            Handler mainHandler = new Handler();
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
            // Bind the player to the view.
            mPlayerView.setPlayer(mExoPlayer);
            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, "yourApplicationName"), bandwidthMeter);
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(videoURL));
            // Prepare the player with the source.
            mExoPlayer.prepare(videoSource);
        } else
            mPlayerView.setVisibility(View.GONE);


        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releaseVideo();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseVideo();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            videoPlaying = mExoPlayer.getPlayWhenReady();
            videoPosition = mExoPlayer.getCurrentPosition();
        }
        if (Util.SDK_INT <= 23) {
            releaseVideo();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //Save player position and if playing
        outState.putLong(BUNDLE_VIDEOPOSITION, videoPosition);
        outState.putBoolean(BUNDLE_VIDEOPLAYING, videoPlaying);
        super.onSaveInstanceState(outState);
    }

    private void initRecyclerView() {
        adapter = new IngredientsAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public void releaseVideo() {
        //Releases video resources to save system resources
        if (mExoPlayer != null) {
            //Set player position and if playing
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

}
