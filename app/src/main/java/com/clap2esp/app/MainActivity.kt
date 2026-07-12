package com.clap2esp.app

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startForegroundService


class MainActivity : AppCompatActivity() {


    private val microphonePermissionCode = 100

    private lateinit var statusText: TextView


    private val clapReceiver = object : BroadcastReceiver() {

        override fun onReceive(
            context: Context?,
            intent: Intent?
        ) {

            statusText.text = "Status: CLAP DETECTED!"

        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


        statusText = findViewById(R.id.statusText)


        checkMicrophonePermission()


        val startButton = findViewById<Button>(
            R.id.startButton
        )


        val stopButton = findViewById<Button>(
            R.id.stopButton
        )



        startButton.setOnClickListener {


            val serviceIntent = Intent(
                this,
                AudioService::class.java
            )


            startForegroundService(
                this,
                serviceIntent
            )

            statusText.text = "Status: Listening..."

        }



        stopButton.setOnClickListener {


            val serviceIntent = Intent(
                this,
                AudioService::class.java
            )


            stopService(serviceIntent)


            statusText.text = "Status: Stopped"

        }


        registerReceiver(
            clapReceiver,
            IntentFilter("CLAP_EVENT")
        )

    }



    private fun checkMicrophonePermission() {


        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {


            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.RECORD_AUDIO
                ),
                microphonePermissionCode
            )

        }

    }



    override fun onDestroy() {

        unregisterReceiver(clapReceiver)

        super.onDestroy()
    }

}
