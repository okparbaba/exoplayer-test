package com.ginntopopin.exoplayertest

import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.exo_player_control_view.*

class MainActivity : AppCompatActivity() {
//    override fun onPrepared() {
//        video_view.start()
//    }
    private lateinit var player: SimpleExoPlayer
    //    lateinit var exoPlayerView:SimpleExoPlayerView
    //    lateinit var exoPlayer:SimpleExoPlayer
    val videolink =
        "http://10.0.2.2:3000/video"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ivwidesmall?.setOnClickListener {
            if (!isWide){
                isWide = true
                Log.e("wide",isWide.toString())
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

            }else{
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                isWide = false

            }


        }

    }
    companion object{
        var isWide = false
    }

    override fun onStart() {
        super.onStart()
        player = ExoPlayerFactory.newSimpleInstance(
            this,
            DefaultTrackSelector()
        )
        player_view.player = player
        val dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "exo-demo"))
        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(videolink))
        player.prepare(mediaSource)
        player.playWhenReady = true
        player.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                if (playbackState == ExoPlayer.STATE_BUFFERING) {
                    loadingVideo.visibility = VISIBLE
                } else {
                    loadingVideo.visibility = GONE
                }
            }

        })
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putLong("Position", player.currentPosition)
    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val position = savedInstanceState.getLong("Position")
        player.seekTo(position)
    }

    override fun onStop() {
        super.onStop()
        player_view.player = null
        player.release()
    }

    override fun onBackPressed() {
        if (isWide){
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            isWide = false
        }else super.onBackPressed()

    }

}
