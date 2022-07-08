package h.k.claptofindmyphone.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.ToneGenerator
import android.os.*
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import h.k.claptofindmyphone.R
import h.k.claptofindmyphone.models.RecordModel
import h.k.claptofindmyphone.ui.MainActivity
import org.tensorflow.lite.task.audio.classifier.AudioClassifier
import java.util.*


class ServiceRecordAudio : Service() {
    private var mNM: NotificationManager? = null
    private var recorder: MediaRecorder? = null
    private lateinit var beeper : ToneGenerator
    private var audioClassifier: AudioClassifier? = null
    private var audioRecord: AudioRecord? = null
    private var classificationInterval = 500L // how often should classification run in milli-secs
    private lateinit var handler: Handler // background thread handler to run classification
    lateinit var sharedPeref: SharedPreferences
    lateinit var sharedPerefEditor: SharedPreferences.Editor
    lateinit var gson: Gson
    lateinit var cameraManager: CameraManager
    lateinit var vibrator: Vibrator
    val myString = "0101010101"
    val blinkDelay: Long = 50

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
            .setSmallIcon(R.drawable.logo) // the status icon
            .setWhen(System.currentTimeMillis()) // the time stamp
            .setContentTitle("Clap To Find My Phone") // the label of the entry
            .setContentIntent(notiIntent) // The intent to send when the entry is clicked
            .build()
        startForeground(159, notification)

        gson = Gson()
        sharedPeref = this.getSharedPreferences(
            "ctfmf", Context.MODE_PRIVATE
        )
        sharedPerefEditor = sharedPeref.edit()
        cameraManager = this.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        vibrator=this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        handler = Handler()
        startAudioClassification()
        //////


    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        mNM?.cancel(159)
        handler.removeCallbacksAndMessages(null)
        audioRecord?.stop()
        audioRecord = null
        audioClassifier = null
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
                    if (filteredModelOutput[0].index == 56 || filteredModelOutput[0].index == 57 || filteredModelOutput[0].index == 58) {
                        beeper = ToneGenerator(AudioManager.STREAM_NOTIFICATION, sharedPeref.getInt("melody_volume_clap",100))

                        ////////
                        var dataArray = ArrayList<RecordModel>()
                        var dataA = sharedPeref.getString("record", null)
                        dataArray =gson.fromJson(dataA, object : TypeToken<ArrayList<RecordModel>>() {}.type)
                        val date1: Date = Date()
                        val date=date1.date.toString() + "/" + (date1.month + 1) + "/" + (date1.year + 1900)
                        val tim=   date1.hours.toString() + ":" + date1.minutes + ":" + date1.seconds
                        dataArray.add(RecordModel("Clap", tim, date))
                        sharedPerefEditor.putString("record", gson.toJson(dataArray))
                        sharedPerefEditor.apply()

                        /////////


                        if (sharedPeref.getBoolean("melody_clap", false)) {
                            beeper.startTone(ToneGenerator.TONE_CDMA_HIGH_SS,sharedPeref.getInt("melody_length_clap",500))
                            Log.e("sss",sharedPeref.getInt("melody_length_clap",500).toString())
                        }
                        if (sharedPeref.getBoolean("flash_clap", false)) {
                            if (this@ServiceRecordAudio.packageManager?.hasSystemFeature(
                                    PackageManager.FEATURE_CAMERA_FLASH
                                ) == true
                            ) {


                                for (i in 0 until myString.length) {
                                    if (myString[i] == '0') {
                                        cameraManager.setTorchMode(
                                            cameraManager.cameraIdList[0],
                                            true
                                        )
                                    } else {
                                        cameraManager.setTorchMode(
                                            cameraManager.cameraIdList[0],
                                            false
                                        )
                                    }
                                    try {
                                        Thread.sleep(blinkDelay)
                                    } catch (e: InterruptedException) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                        }
                        if (sharedPeref.getBoolean("vibrate_clap",false)){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator.vibrate(VibrationEffect.createOneShot(sharedPeref.getInt("melody_length_clap",500).toLong(), VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                //deprecated in API 26
                                vibrator.vibrate(sharedPeref.getInt("melody_length_clap",500).toLong());
                            }
                        }

                    Log.e(
                        "qq",
                        filteredModelOutput[0].label.toString() + " , " + filteredModelOutput[0].index.toString()
                    )
                }
                    else if (filteredModelOutput[0].index == 426 || filteredModelOutput[0].index == 479 || filteredModelOutput[0].index == 396 || filteredModelOutput[0].index == 79|| filteredModelOutput[0].index == 35) {
                        beeper = ToneGenerator(AudioManager.STREAM_NOTIFICATION, sharedPeref.getInt("melody_volume_whistle",100))
                       ///////

                        var dataArray = ArrayList<RecordModel>()
                        var dataA = sharedPeref.getString("record", null)
                        dataArray =gson.fromJson(dataA, object : TypeToken<ArrayList<RecordModel>>() {}.type)
                        val date1: Date = Date()
                        val date=date1.date.toString() + "/" + (date1.month + 1) + "/" + (date1.year + 1900)
                        val tim=   date1.hours.toString() + ":" + date1.minutes + ":" + date1.seconds
                        dataArray.add(RecordModel("Whistle", tim, date))
                        sharedPerefEditor.putString("record", gson.toJson(dataArray))
                        sharedPerefEditor.apply()
                        //////////////////////



                        if (sharedPeref.getBoolean("melody_whistle", false)) {
                            beeper.startTone(ToneGenerator.TONE_CDMA_HIGH_L,sharedPeref.getInt("melody_length_whistle",500))
                        }
                        if (sharedPeref.getBoolean("flash_whistle", false)) {
                            if (this@ServiceRecordAudio.packageManager?.hasSystemFeature(
                                    PackageManager.FEATURE_CAMERA_FLASH
                                ) == true
                            ) {


                                for (i in 0 until myString.length) {
                                    if (myString[i] == '0') {
                                        cameraManager.setTorchMode(
                                            cameraManager.cameraIdList[0],
                                            true
                                        )
                                    } else {
                                        cameraManager.setTorchMode(
                                            cameraManager.cameraIdList[0],
                                            false
                                        )
                                    }
                                    try {
                                        Thread.sleep(blinkDelay)
                                    } catch (e: InterruptedException) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                        }
                        if (sharedPeref.getBoolean("vibrate_whistle",false)){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator.vibrate(VibrationEffect.createOneShot(sharedPeref.getInt("melody_length_whistle",500).toLong(), VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                //deprecated in API 26
                                vibrator.vibrate(sharedPeref.getInt("melody_length_whistle",500).toLong())
                            }
                        }

                        Log.e(
                            "qq",
                            filteredModelOutput[0].label.toString() + " , " + filteredModelOutput[0].index.toString()
                        )
                    }


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