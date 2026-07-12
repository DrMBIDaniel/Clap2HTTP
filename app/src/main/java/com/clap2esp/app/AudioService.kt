package com.clap2esp.app

import android.app.Service
import android.content.Intent
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.IBinder
import android.util.Log

class AudioService : Service() {

    private var audioRecord: AudioRecord? = null
    private var isRecording = false

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        startListening()

        return START_STICKY
    }


    private fun startListening() {

        val bufferSize = AudioRecord.getMinBufferSize(
            44100,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )


        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            44100,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )


        audioRecord?.startRecording()

        isRecording = true


        Thread {

            val buffer = ShortArray(bufferSize)

            while (isRecording) {

                val read = audioRecord?.read(
                    buffer,
                    0,
                    buffer.size
                )


                if (read != null && read > 0) {

                    val maxAmplitude = buffer.maxOrNull()

                    Log.d(
                        "CLAP",
                        "Sound level: $maxAmplitude"
                    )
                }
            }

        }.start()
    }


    override fun onDestroy() {

        isRecording = false

        audioRecord?.stop()
        audioRecord?.release()

        audioRecord = null

        super.onDestroy()
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
