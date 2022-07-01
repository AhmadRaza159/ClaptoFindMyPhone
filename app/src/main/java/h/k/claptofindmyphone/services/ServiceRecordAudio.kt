package h.k.claptofindmyphone.services

import android.app.*
import android.content.Intent
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.os.HandlerCompat
import h.k.claptofindmyphone.MainActivity
import h.k.claptofindmyphone.R
import h.k.claptofindmyphone.databinding.ActivityMainBinding
import org.tensorflow.lite.task.audio.classifier.AudioClassifier
import java.lang.String
import kotlin.Double


class ServiceRecordAudio : Service() {
    private var mNM: NotificationManager? = null
    private var recorder: MediaRecorder? = null

    private var audioClassifier: AudioClassifier? = null
    private var audioRecord: AudioRecord? = null
    private var classificationInterval = 500L // how often should classification run in milli-secs
    private lateinit var handler: Handler // background thread handler to run classification
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
//        val handlerThread = HandlerThread("backgroundThread")
//        handlerThread.start()
//        handler = HandlerCompat.createAsync(handlerThread.looper)

        handler=Handler()
        startAudioClassification()
        //////
//        recorder = MediaRecorder()
//        recorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
//        recorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
//        recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
//        recorder!!.setOutputFile("/data/data/$packageName/music.3gp")
//
//        recorder!!.prepare()
//        recorder!!.start()
//        val handler = Handler()
//        handler.postDelayed(object : Runnable {
//            override fun run() {
//                Log.e("firstday",recorder!!.maxAmplitude.toString())
//
//                handler.postDelayed(this, 1000)
//            }
//        }, 1000)
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
//        Log.e("firstday",recorder!!.maxAmplitude.toString())
//        recorder!!.stop();
//        recorder!!.release();
    }

    private fun startAudioClassification() {
        // If the audio classifier is initialized and running, do nothing.
        if (audioClassifier != null) return;

        // Initialize the audio classifier
        val classifier = AudioClassifier.createFromFile(this, "cmcm.tflite")
        val audioTensor = classifier.createInputTensorAudio()

        // Initialize the audio recorder
        val record = classifier.createAudioRecord()
        record.startRecording()

        // Define the classification runnable
        val run = object : Runnable {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun run() {
                val startTime = System.currentTimeMillis()

                // Load the latest audio sample
                audioTensor.load(record)
                val output = classifier.classify(audioTensor)

                // Filter out results above a certain threshold, and sort them descendingly
                val filteredModelOutput = output[0].categories.filter {
                    it.score > 0.3f
                }.sortedBy {
                    -it.score
                }


                val finishTime = System.currentTimeMillis()
                if (filteredModelOutput.size > 0) {
                    Log.e(
                        "qq",
                        filteredModelOutput[0].label.toString() + " , " + filteredModelOutput[0].index.toString()
                    )

                }
                Log.d("TAG", "Latency = ${finishTime - startTime}ms")

                // Updating the UI

                // Rerun the classification after a certain interval
                handler.postDelayed(this, classificationInterval)
            }
        }

        // Start the classification process
        handler.post(run)

        // Save the instances we just created for use later
        audioClassifier = classifier
        audioRecord = record
    }
}