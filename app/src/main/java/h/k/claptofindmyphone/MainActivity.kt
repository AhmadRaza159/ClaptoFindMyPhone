package h.k.claptofindmyphone

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import h.k.claptofindmyphone.databinding.ActivityMainBinding
import h.k.claptofindmyphone.services.ServiceRecordAudio

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.start.setOnClickListener {
            if (!isServiceRunning(ServiceRecordAudio::class.java)){
                val i = Intent(this, ServiceRecordAudio::class.java)
                startForegroundService(i)
            }
        }

        binding.stop.setOnClickListener {
            stopService(Intent(this, ServiceRecordAudio::class.java))
        }
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