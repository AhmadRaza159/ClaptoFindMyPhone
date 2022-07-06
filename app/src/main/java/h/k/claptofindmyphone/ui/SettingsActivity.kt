package h.k.claptofindmyphone.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import h.k.claptofindmyphone.R
import h.k.claptofindmyphone.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private var selectedItem=0
    lateinit var sharedPeref: SharedPreferences
    lateinit var sharedPerefEditor: SharedPreferences.Editor
    lateinit var gson: Gson
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
                } else {
                    sharedPerefEditor.putBoolean("flash_clap", false)
                    binding.btnFlash.setImageDrawable(resources.getDrawable(R.drawable.flash_off_btn))
                    sharedPerefEditor.apply()
                }
            }
            else{
                if (isChecked) {
                    sharedPerefEditor.putBoolean("flash_whistle", true)
                    binding.btnFlash.setImageDrawable(resources.getDrawable(R.drawable.flash_on_btn))
                    sharedPerefEditor.apply()
                } else {
                    sharedPerefEditor.putBoolean("flash_whistle", false)
                    binding.btnFlash.setImageDrawable(resources.getDrawable(R.drawable.flash_off_btn))
                    sharedPerefEditor.apply()
                }
            }
        }

        binding.switchVibrate.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (selectedItem==1) {
                if (isChecked) {
                    sharedPerefEditor.putBoolean("vibrate_clap", true)
                    binding.btnVibrate.setImageDrawable(resources.getDrawable(R.drawable.vibrate_on_btn))
                    sharedPerefEditor.apply()
                } else {
                    sharedPerefEditor.putBoolean("vibrate_clap", false)
                    binding.btnVibrate.setImageDrawable(resources.getDrawable(R.drawable.vibrate_off_btn))
                    sharedPerefEditor.apply()
                }
            }
            else{
                if (isChecked) {
                    sharedPerefEditor.putBoolean("vibrate_whistle", true)
                    binding.btnVibrate.setImageDrawable(resources.getDrawable(R.drawable.vibrate_on_btn))
                    sharedPerefEditor.apply()
                } else {
                    sharedPerefEditor.putBoolean("vibrate_whistle", false)
                    binding.btnVibrate.setImageDrawable(resources.getDrawable(R.drawable.vibrate_off_btn))
                    sharedPerefEditor.apply()
                }
            }
        }

        binding.switchMelody.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (selectedItem==1) {
                if (isChecked) {
                    sharedPerefEditor.putBoolean("melody_clap", true)
                    binding.btnMelody.setImageDrawable(resources.getDrawable(R.drawable.melody_on_btn))
                    sharedPerefEditor.apply()
                } else {
                    sharedPerefEditor.putBoolean("melody_clap", false)
                    binding.btnMelody.setImageDrawable(resources.getDrawable(R.drawable.melody_off_btn))
                    sharedPerefEditor.apply()
                }
            }
            else{
                if (isChecked) {
                    sharedPerefEditor.putBoolean("melody_whistle", true)
                    binding.btnMelody.setImageDrawable(resources.getDrawable(R.drawable.melody_on_btn))
                    sharedPerefEditor.apply()
                } else {
                    sharedPerefEditor.putBoolean("melody_whistle", false)
                    binding.btnMelody.setImageDrawable(resources.getDrawable(R.drawable.melody_off_btn))
                    sharedPerefEditor.apply()
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
    }

    private fun loadClapSetting() {
        binding.switchFlash.isChecked = sharedPeref.getBoolean("flash_clap",false)
        binding.switchVibrate.isChecked = sharedPeref.getBoolean("vibrate_clap",false)
        binding.switchMelody.isChecked = sharedPeref.getBoolean("melody_clap",false)
    }

    private fun loadWhistleSetting() {
        binding.switchFlash.isChecked = sharedPeref.getBoolean("flash_whistle",false)
        binding.switchVibrate.isChecked = sharedPeref.getBoolean("vibrate_whistle",false)
        binding.switchMelody.isChecked = sharedPeref.getBoolean("melody_whistle",false)
    }
}