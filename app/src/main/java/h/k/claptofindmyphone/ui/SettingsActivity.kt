package h.k.claptofindmyphone.ui

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import h.k.claptofindmyphone.R
import h.k.claptofindmyphone.databinding.ActivitySettingsBinding
import h.k.claptofindmyphone.services.ServiceRecordAudio

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private var selectedItem=0
    lateinit var sharedPeref: SharedPreferences
    lateinit var sharedPerefEditor: SharedPreferences.Editor
    lateinit var gson: Gson

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clickListeners()

        gson = Gson()
        sharedPeref = this.getSharedPreferences(
            "ctfmf", Context.MODE_PRIVATE
        )
        sharedPerefEditor = sharedPeref.edit()
        binding.clapBar.visibility=View.INVISIBLE

        loadWhistleSetting()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun clickListeners() {
        binding.toolbarClap.setOnClickListener {
            selectedItem=1
            binding.clapBar.visibility=View.VISIBLE
            binding.whistleBar.visibility=View.INVISIBLE
            loadClapSetting()

        }
        binding.toolbarWhistle.setOnClickListener {
            selectedItem=0
            binding.clapBar.visibility=View.INVISIBLE
            binding.whistleBar.visibility=View.VISIBLE
            loadWhistleSetting()

        }


        binding.switchFlash.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (selectedItem==1) {
                if (isChecked) {
                    sharedPerefEditor.putBoolean("flash_clap", true)
                    binding.btnFlash.setImageDrawable(resources.getDrawable(R.drawable.flash_on_btn))
                    sharedPerefEditor.apply()
                    if (isServiceRunning(ServiceRecordAudio::class.java)) {
                        val i = Intent(this@SettingsActivity, ServiceRecordAudio::class.java)
                        startForegroundService(i)
                    }
                } else {
                    sharedPerefEditor.putBoolean("flash_clap", false)
                    binding.btnFlash.setImageDrawable(resources.getDrawable(R.drawable.flash_off_btn))
                    sharedPerefEditor.apply()
                    if (isServiceRunning(ServiceRecordAudio::class.java)) {
                        val i = Intent(this@SettingsActivity, ServiceRecordAudio::class.java)
                        startForegroundService(i)
                    }
                }
            }
            else{
                if (isChecked) {
                    sharedPerefEditor.putBoolean("flash_whistle", true)
                    binding.btnFlash.setImageDrawable(resources.getDrawable(R.drawable.flash_on_btn))
                    sharedPerefEditor.apply()
                    if (isServiceRunning(ServiceRecordAudio::class.java)) {
                        val i = Intent(this@SettingsActivity, ServiceRecordAudio::class.java)
                        startForegroundService(i)
                    }
                } else {
                    sharedPerefEditor.putBoolean("flash_whistle", false)
                    binding.btnFlash.setImageDrawable(resources.getDrawable(R.drawable.flash_off_btn))
                    sharedPerefEditor.apply()
                    if (isServiceRunning(ServiceRecordAudio::class.java)) {
                        val i = Intent(this@SettingsActivity, ServiceRecordAudio::class.java)
                        startForegroundService(i)
                    }
                }
            }
        }

        binding.switchVibrate.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (selectedItem==1) {
                if (isChecked) {
                    sharedPerefEditor.putBoolean("vibrate_clap", true)
                    binding.btnVibrate.setImageDrawable(resources.getDrawable(R.drawable.vibrate_on_btn))
                    sharedPerefEditor.apply()
                    if (isServiceRunning(ServiceRecordAudio::class.java)) {
                        val i = Intent(this@SettingsActivity, ServiceRecordAudio::class.java)
                        startForegroundService(i)
                    }
                } else {
                    sharedPerefEditor.putBoolean("vibrate_clap", false)
                    binding.btnVibrate.setImageDrawable(resources.getDrawable(R.drawable.vibrate_off_btn))
                    sharedPerefEditor.apply()
                    if (isServiceRunning(ServiceRecordAudio::class.java)) {
                        val i = Intent(this@SettingsActivity, ServiceRecordAudio::class.java)
                        startForegroundService(i)
                    }
                }
            }
            else{
                if (isChecked) {
                    sharedPerefEditor.putBoolean("vibrate_whistle", true)
                    binding.btnVibrate.setImageDrawable(resources.getDrawable(R.drawable.vibrate_on_btn))
                    sharedPerefEditor.apply()
                    if (isServiceRunning(ServiceRecordAudio::class.java)) {
                        val i = Intent(this@SettingsActivity, ServiceRecordAudio::class.java)
                        startForegroundService(i)
                    }
                } else {
                    sharedPerefEditor.putBoolean("vibrate_whistle", false)
                    binding.btnVibrate.setImageDrawable(resources.getDrawable(R.drawable.vibrate_off_btn))
                    sharedPerefEditor.apply()
                    if (isServiceRunning(ServiceRecordAudio::class.java)) {
                        val i = Intent(this@SettingsActivity, ServiceRecordAudio::class.java)
                        startForegroundService(i)
                    }
                }
            }
        }

        binding.switchMelody.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (selectedItem==1) {
                if (isChecked) {
                    sharedPerefEditor.putBoolean("melody_clap", true)
                    binding.btnMelody.setImageDrawable(resources.getDrawable(R.drawable.melody_on_btn))
                    sharedPerefEditor.apply()
                    if (isServiceRunning(ServiceRecordAudio::class.java)) {
                        val i = Intent(this@SettingsActivity, ServiceRecordAudio::class.java)
                        startForegroundService(i)
                    }
                } else {
                    sharedPerefEditor.putBoolean("melody_clap", false)
                    binding.btnMelody.setImageDrawable(resources.getDrawable(R.drawable.melody_off_btn))
                    sharedPerefEditor.apply()
                    if (isServiceRunning(ServiceRecordAudio::class.java)) {
                        val i = Intent(this@SettingsActivity, ServiceRecordAudio::class.java)
                        startForegroundService(i)
                    }
                }
            }
            else{
                if (isChecked) {
                    sharedPerefEditor.putBoolean("melody_whistle", true)
                    binding.btnMelody.setImageDrawable(resources.getDrawable(R.drawable.melody_on_btn))
                    sharedPerefEditor.apply()
                    if (isServiceRunning(ServiceRecordAudio::class.java)) {
                        val i = Intent(this@SettingsActivity, ServiceRecordAudio::class.java)
                        startForegroundService(i)
                    }
                } else {
                    sharedPerefEditor.putBoolean("melody_whistle", false)
                    binding.btnMelody.setImageDrawable(resources.getDrawable(R.drawable.melody_off_btn))
                    sharedPerefEditor.apply()
                    if (isServiceRunning(ServiceRecordAudio::class.java)) {
                        val i = Intent(this@SettingsActivity, ServiceRecordAudio::class.java)
                        startForegroundService(i)
                    }
                }
            }
        }

        binding.btnFlash.setOnClickListener {
            binding.switchFlash.isChecked=!binding.switchFlash.isChecked
        }
        binding.btnVibrate.setOnClickListener {
            binding.switchVibrate.isChecked=!binding.switchVibrate.isChecked
        }
        binding.btnMelody.setOnClickListener {
            binding.switchMelody.isChecked=!binding.switchMelody.isChecked
        }
        binding.lengthSeekbar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                var prog=p1
                if (prog==0) prog=1
                if (selectedItem==1) {
                    sharedPerefEditor.putInt("melody_length_clap", prog * 50)
                }
                else{
                    sharedPerefEditor.putInt("melody_length_whistle", prog * 50)
                }
                sharedPerefEditor.apply()
                if (isServiceRunning(ServiceRecordAudio::class.java)) {
                    val i = Intent(this@SettingsActivity, ServiceRecordAudio::class.java)
                    startForegroundService(i)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onStopTrackingTouch(p0: SeekBar?) {
                var prog=p0?.progress
                if (prog==0) prog=1
                if (selectedItem==1) {
                    sharedPerefEditor.putInt("melody_length_clap", prog?.times(50) ?: 500)
                }
                else{
                    sharedPerefEditor.putInt("melody_length_whistle", prog?.times(50) ?: 500)
                }
                sharedPerefEditor.apply()
                if (isServiceRunning(ServiceRecordAudio::class.java)) {
                    val i = Intent(this@SettingsActivity, ServiceRecordAudio::class.java)
                    startForegroundService(i)
                }
            }

        })


        binding.volumeSeekbar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                var prog=p1
                if (prog==0) prog=1
                if (selectedItem==1) {
                    sharedPerefEditor.putInt("melody_volume_clap", prog )
                }
                else{
                    sharedPerefEditor.putInt("melody_volume_whistle", prog)
                }
                sharedPerefEditor.apply()
                if (isServiceRunning(ServiceRecordAudio::class.java)) {
                    val i = Intent(this@SettingsActivity, ServiceRecordAudio::class.java)
                    startForegroundService(i)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onStopTrackingTouch(p0: SeekBar?) {
                var prog=p0?.progress
                if (prog==0) prog=1
                if (selectedItem==1) {
                    sharedPerefEditor.putInt("melody_volume_clap", prog?:100 )
                }
                else{
                    sharedPerefEditor.putInt("melody_volume_whistle", prog?:100 )
                }
                sharedPerefEditor.apply()
                if (isServiceRunning(ServiceRecordAudio::class.java)) {
                    val i = Intent(this@SettingsActivity, ServiceRecordAudio::class.java)
                    startForegroundService(i)
                }
            }

        })


    }

    private fun loadClapSetting() {
        binding.switchFlash.isChecked = sharedPeref.getBoolean("flash_clap",false)
        binding.switchVibrate.isChecked = sharedPeref.getBoolean("vibrate_clap",false)
        binding.switchMelody.isChecked = sharedPeref.getBoolean("melody_clap",false)
        binding.lengthSeekbar.progress=sharedPeref.getInt("melody_length_clap",500)/50
        binding.volumeSeekbar.progress=sharedPeref.getInt("melody_volume_clap",100)
    }

    private fun loadWhistleSetting() {
        binding.switchFlash.isChecked = sharedPeref.getBoolean("flash_whistle",false)
        binding.switchVibrate.isChecked = sharedPeref.getBoolean("vibrate_whistle",false)
        binding.switchMelody.isChecked = sharedPeref.getBoolean("melody_whistle",false)
        binding.lengthSeekbar.progress=sharedPeref.getInt("melody_length_whistle",500)/50
        binding.volumeSeekbar.progress=sharedPeref.getInt("melody_volume_whistle",100)
    }
    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}