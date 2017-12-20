package com.example.anish.spotifyalarm;

/**
 * Created by anish on 12/17/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.util.SortedList;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.GET;


public class SpotUtils extends Activity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback, View.OnClickListener {

    private static final String CLIENT_ID = "33d8356cad404b4e8b50a315623ea1dc";
    private static final String REDIRECT_URI = "myspotifyalarm://callback";
    private static final int REQUEST_CODE = 1994;

    private String NOW_PLAYING = "spotify:track:1F5A4LpF8ylUVtPoMAvNvn";
    private Button cancelButton;
    private Player mPlayer;

/*

    public static final String BASE_URL = "https://api.spotify.com/v1/search";
    public static final String API_KEY = "Authorization";

    public static String buildSpotURL (String trackSearch) {
        Log.d("SpotUtils", "Got a query for - " + trackSearch);

        return Uri.parse(BASE_URL).buildUpon()
                .appendPath("search")
                .appendQueryParameter("q", trackSearch)
                .appendQueryParameter("type", "track")
                .appendQueryParameter("limit", "10")
                .build()
                .toString();
    } */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this);

    }

    public void onClick(View v) {

        if(v == cancelButton) {
            mPlayer.pause(null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            SpotifyApi api = new SpotifyApi();
            api.setAccessToken(response.getAccessToken());

            SpotifyService service = api.getService();
            service.searchTracks("Time to Pretend", new Callback<TracksPager>() {

                @Override
                public void success(TracksPager results, retrofit.client.Response response) {
                    TracksPager trackList = results;
                    List<Track> trackResult = trackList.tracks.items;

                    for (int i = 0; i < trackResult.size(); i++) {
                        Track curTrack = trackResult.get(i);
                        Log.i("SpotUtils", i + " " + curTrack.toString());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                }
            });

/*            List<Track> trackResult = trackList.tracks.items;

            for (int i = 0; i < trackResult.size(); i++) {
                Track curTrack = trackResult.get(i);
                Log.i("SpotUtils", i + " " + curTrack.toString());
            } */

            Log.d("SpotUtils", "Response here is: " + response.getAccessToken());

            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(SpotUtils.this);
                        mPlayer.addNotificationCallback(SpotUtils.this);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    public void stopMusic() {
        mPlayer.pause(null);
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("MainActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");

        mPlayer.playUri(null, NOW_PLAYING, 0, 0);
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

}
