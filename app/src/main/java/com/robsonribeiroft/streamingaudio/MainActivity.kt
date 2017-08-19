package com.robsonribeiroft.streamingaudio

import android.app.ProgressDialog
import android.media.AudioManager
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private var button: ImageButton? = null
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.btn_play) as ImageButton

        config()

        button!!.setOnClickListener({
            if (mediaPlayer!!.isPlaying) pause() else play()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
    }

    private fun config(){
        try {
            mediaPlayer = MediaPlayer()
            progressDialog = ProgressDialog(this)
            progressDialog!!.setMessage(MESSAGE)
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
            mediaPlayer!!.isLooping = true
            mediaPlayer!!.run {
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setOnPreparedListener({
                    mediaPlayer!!.start()
                    progressDialog!!.dismiss()
                })
                setOnErrorListener({ mp, what, extra ->
                    Log.i(TAG, "mp: $mp\nwhat: $what\nextra: $extra")
                    stop()
                    config()
                    false
                })
                setDataSource(url)
                prepareAsync()
                setOnCompletionListener {
                    progressDialog!!.dismiss()
                }
            }
        } catch (e : Exception){
            e.message
        }
    }

    private fun play() {
        mediaPlayer!!.start()
        button!!.setImageResource(android.R.drawable.ic_media_pause)
    }

    private fun pause() {
        mediaPlayer!!.pause()
        button!!.setImageResource(android.R.drawable.ic_media_play)
    }

    private fun stop(){
        mediaPlayer!!.stop()
        mediaPlayer!!.release()
        mediaPlayer = null
    }


    companion object {
        //Beach Park's Radio
        private val url = "http://www.radioideal.net:8026/;stream/1"
        private val TAG = "DAMIT"
        private val MESSAGE = "Conecting"
    }
}
