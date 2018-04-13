package com.bussinesspoolapp.bussipool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import com.beans.SchemeBean;
import com.bussinesspoolapp.bussipool.ServiceUtil.CallPost;
import com.bussinesspoolapp.bussipool.interfaces.AsyncResponse;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;


public class Video extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener,AsyncResponse {
    public static final String API_KEY = "AIzaSyCOhK9d-cR3AeoxCN06q2ss8euTKs5IvBA";
    public static String VIDEO_ID = "";
    String type="";
    long userId;
    int schemeId;
    private static final String PREFS_NAME = "preferences";
    ObjectMapper objectMapper
            = new ObjectMapper();
    TextView txtVideoSchemeDesc,txtVideoSchemeName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        userId= settings.getLong("userId",0);
        Intent intent = getIntent();
        schemeId = intent.getExtras().getInt("schemeId");
        txtVideoSchemeDesc = findViewById(R.id.txtVideoSchemeDesc);
        txtVideoSchemeName = findViewById(R.id.txtVideoSchemeName);
        txtVideoSchemeDesc.setMovementMethod(new ScrollingMovementMethod());
        CallPost callPost=new CallPost(this);
        type="schemeDetails";
        callPost.execute("/scheme/getschemedetail",""+schemeId);
    }
    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult result) {
        Toast.makeText(this, "Failured to Initialize!", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
/** add listeners to YouTubePlayer instance **/
        player.setPlayerStateChangeListener(playerStateChangeListener);
        player.setPlaybackEventListener(playbackEventListener);
/** Start buffering **/
        if (!wasRestored) {
            player.cueVideo(VIDEO_ID);
        }
    }
    private PlaybackEventListener playbackEventListener = new PlaybackEventListener() {
        @Override
        public void onBuffering(boolean arg0) {
        }
        @Override
        public void onPaused() {
        }
        @Override
        public void onPlaying() {
        }
        @Override
        public void onSeekTo(int arg0) {
        }
        @Override
        public void onStopped() {
        }
    };
    private PlayerStateChangeListener playerStateChangeListener = new PlayerStateChangeListener() {
        @Override
        public void onAdStarted() {
        }
        @Override
        public void onError(ErrorReason arg0) {
        }
        @Override
        public void onLoaded(String arg0) {
        }
        @Override
        public void onLoading() {
        }
        @Override
        public void onVideoEnded() {
        }
        @Override
        public void onVideoStarted() {
        }
    };

    @Override
    public void processFinish(String output) {
        if(type.equals("schemeDetails")) {
            SchemeBean up = null;
            try {
                up = objectMapper.readValue(output, SchemeBean.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            txtVideoSchemeName.setText(up.getSchemeName());
            txtVideoSchemeDesc.setText(up.getSchemeDescription());
//                schemeStartDate.setText(up.getStartDate());
            VIDEO_ID = up.getVideoId();
            /** Initializing YouTube Player View **/
            YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
            youTubePlayerView.initialize(API_KEY, this);
//                schemeMemberPerc.setText(up.getMemberPerc() + " %");
        }
    }
}
