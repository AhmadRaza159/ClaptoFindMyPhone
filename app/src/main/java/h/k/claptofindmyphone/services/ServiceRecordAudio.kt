package h.k.claptofindmyphone.services

import android.app.*
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import h.k.claptofindmyphone.MainActivity
import h.k.claptofindmyphone.R
import java.lang.String
import kotlin.Double


class ServiceRecordAudio : Service() {
    private var mNM: NotificationManager? = null
    private var recorder: MediaRecorder? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        mNM = getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        var notiIntent: PendingIntent? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            notiIntent =
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_MUTABLE
                )
        } else {
            notiIntent =
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_ONE_SHOT
                )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(
                "1",
                "a", NotificationManager.IMPORTANCE_NONE
            )
            mNM?.createNotificationChannel(chan)
        } else {
            ""
        }
        val notification: Notification = Notification.Builder(this, "1")
            .setSmallIcon(R.drawable.ic_launcher_background) // the status icon
            .setWhen(System.currentTimeMillis()) // the time stamp
            .setContentTitle("Assistive Touch") // the label of the entry
            .setContentIntent(notiIntent) // The intent to send when the entry is clicked
            .build()
        startForeground(159, notification)


        //////
        recorder = MediaRecorder()
        recorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        recorder!!.setOutputFile("/data/data/$packageName/music.3gp")

        recorder!!.prepare()
        recorder!!.start()
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                Log.e("firstday",recorder!!.maxAmplitude.toString())

                handler.postDelayed(this, 1000)
            }
        }, 1000)
//        recorder!!.setOnInfoListener { mediaRecorder, i, i2 ->
//
//        }

    }
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
    override fun onDestroy() {
        super.onDestroy()
        mNM?.cancel(159)
        Log.e("firstday",recorder!!.maxAmplitude.toString())
        recorder!!.stop();
        recorder!!.release();
    }
}