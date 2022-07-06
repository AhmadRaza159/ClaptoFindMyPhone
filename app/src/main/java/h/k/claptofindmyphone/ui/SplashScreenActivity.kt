package h.k.claptofindmyphone.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import h.k.claptofindmyphone.R
import h.k.claptofindmyphone.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private var recordAudioPermission = arrayOf(
        Manifest.permission.RECORD_AUDIO
    )
    lateinit var sharedPeref: SharedPreferences
    lateinit var sharedPerefEditor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPeref = this.getSharedPreferences(
            "ctfmf", Context.MODE_PRIVATE
        )
        sharedPerefEditor = sharedPeref.edit()
        Handler().postDelayed({
            binding.lotti.visibility=View.INVISIBLE
            binding.extendedFloatingActionButton.visibility=View.VISIBLE

        }, 3000)
        binding.extendedFloatingActionButton.setOnClickListener {
            if (checkRecordAudioPermission()){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            else{
                reqRecordAudioPermission()
            }
        }
    }


    fun checkRecordAudioPermission(): Boolean {
        var a = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        return a
    }
    fun reqRecordAudioPermission() {
        if (sharedPeref.contains("permission2")) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                requestPermissions(recordAudioPermission, 22)
            } else {
                Toast.makeText(
                    this,
                    "Please allow record audio permission!",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts(
                    "package", packageName,
                    null
                )
                intent.data = uri
                this.startActivity(intent)
            }
        } else {
            requestPermissions(recordAudioPermission, 22)
            sharedPerefEditor.putInt("permission2", 1)
            sharedPerefEditor.apply()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (checkRecordAudioPermission()){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

}