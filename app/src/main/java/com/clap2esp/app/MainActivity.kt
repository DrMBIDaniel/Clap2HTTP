package com.clap2esp.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.Intent
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private val microphonePermissionCode = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val startButton = findViewById<Button>(R.id.startButton)

startButton.setOnClickListener {

    val serviceIntent = Intent(
        this,
        AudioService::class.java
    )

    startService(serviceIntent)
}

        checkMicrophonePermission()
    }

    private fun checkMicrophonePermission() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                microphonePermissionCode
            )
        }
    }
}
