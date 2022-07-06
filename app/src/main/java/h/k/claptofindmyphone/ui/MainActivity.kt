package h.k.claptofindmyphone.ui

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import h.k.claptofindmyphone.R
import h.k.claptofindmyphone.databinding.ActivityMainBinding
import h.k.claptofindmyphone.services.ServiceRecordAudio

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var sharedPeref: SharedPreferences
    lateinit var sharedPerefEditor: SharedPreferences.Editor

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPeref = this.getSharedPreferences(
            "ctfmf", Context.MODE_PRIVATE
        )
        sharedPerefEditor = sharedPeref.edit()
        animateNavigationDrawer()
        menuPoping()
        if (!sharedPeref.contains("flash_clap")){
            sharedPerefEditor.putBoolean("flash_whistle", false)
            sharedPerefEditor.putBoolean("flash_clap", false)
            sharedPerefEditor.putBoolean("vibrate_whistle", false)
            sharedPerefEditor.putBoolean("vibrate_clap", false)
            sharedPerefEditor.putBoolean("melody_whistle", true)
            sharedPerefEditor.putBoolean("melody_clap", true)
            sharedPerefEditor.putInt("melody_length_clap", 500)
            sharedPerefEditor.putInt("melody_length_whistle", 500)
            sharedPerefEditor.putInt("melody_volume_whistle", 100)
            sharedPerefEditor.putInt("melody_volume_clap", 100)

        }

        binding.switchBtn.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked){
                if (!isServiceRunning(ServiceRecordAudio::class.java)){
                    val i = Intent(this, ServiceRecordAudio::class.java)
                    startForegroundService(i)
                    binding.switchText.text="Stop"
                }
            }
            else{
                stopService(Intent(this, ServiceRecordAudio::class.java))
                binding.switchText.text="Start"

            }
        }


        binding.btnOnOff.setOnClickListener {
            binding.switchBtn.isChecked = !binding.switchBtn.isChecked
        }

        binding.btnSetting.setOnClickListener {
            startActivity(Intent(this,SettingsActivity::class.java))
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

    private fun animateNavigationDrawer() {

        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        //drawerLayout.setScrimColor(Color.TRANSPARENT);
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                // Scale the View based on current slide offset
                val diffScaledOffset = slideOffset * (1 - 0.7f)
                val offsetScale = 1 - diffScaledOffset
                binding.mainMen.scaleX = offsetScale
                binding.mainMen.scaleY = offsetScale

                // Translate the View, accounting for the scaled width
                val xOffset = drawerView.width * slideOffset
                val xOffsetDiff =binding.mainMen.width * diffScaledOffset / 2
                val xTranslation = xOffset - xOffsetDiff
                binding.mainMen.translationX = xTranslation
            }
        })
    }
    private fun menuPoping() {
        binding.animationView.setOnClickListener {
//            Toast.makeText(this,"TTT",Toast.LENGTH_SHORT).show()
            if (binding.drawerLayout.isDrawerVisible(
                    GravityCompat.START
                )
            ) binding.drawerLayout.closeDrawer(GravityCompat.START) else binding.drawerLayout.openDrawer(
                GravityCompat.START
            )
            /////////////////////////////////
            binding.navView.bringToFront()

            binding.navView.setNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.home -> {
                        binding.drawerLayout.closeDrawers()
                        true
                    }
                    R.id.more_apps -> {
                        val uri =
                            Uri.parse("https://play.google.com/store/apps/developer?id=Westminster+Pro+Apps")
                        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(
                            Intent.FLAG_ACTIVITY_NO_HISTORY or
                                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                        )
                        try {
                            startActivity(goToMarket)
                        } catch (e: ActivityNotFoundException) {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/developer?id=Westminster+Pro+Apps")
                                )
                            )
                        }
                        binding.drawerLayout.closeDrawers()
                        true
                    }
                    R.id.share -> {
                        val sendIntent = Intent()
                        sendIntent.action = Intent.ACTION_SEND
                        sendIntent.putExtra(
                            Intent.EXTRA_TEXT,
                            "Hey check out this app at: https://play.google.com/store/apps/details?id=" + applicationContext.packageName
                        )
                        sendIntent.type = "text/plain"
                        startActivity(sendIntent)
                        binding.drawerLayout.closeDrawers()
                        true
                    }
                    R.id.rate -> {
                        val uri = Uri.parse("market://details?id=" + applicationContext.packageName)
                        val goToMarket = Intent(Intent.ACTION_VIEW, uri)

                        goToMarket.addFlags(
                            Intent.FLAG_ACTIVITY_NO_HISTORY or
                                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                        )
                        try {
                            startActivity(goToMarket)
                        } catch (e: ActivityNotFoundException) {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + applicationContext.packageName)
                                )
                            )
                        }
                        binding.drawerLayout.closeDrawers()
                        true
                    }
                    R.id.policy -> {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://westminsterproapps.blogspot.com/2022/04/privacy-policy.html"))
                        startActivity(browserIntent)
                        binding.drawerLayout.closeDrawers()
                        true
                    }
                    R.id.exit->{
                        binding.drawerLayout.closeDrawers()
                        val alertDialog = AlertDialog.Builder(this)
                        val customLayout: View = getLayoutInflater().inflate(R.layout.dialog, null)
                        alertDialog.setView(customLayout)
                        val alert = alertDialog.create()
                        alert.setCancelable(false)
                        alert.setCanceledOnTouchOutside(true)
                        val yesBtn: TextView = customLayout.findViewById(R.id.btn_yes)
                        val noBtn: TextView = customLayout.findViewById(R.id.btn_no)

                        noBtn.setOnClickListener {
                            alert.dismiss()
                        }
                        yesBtn.setOnClickListener {
                            finishAffinity()
                        }

                        alert.show()
                        true
                    }

                    else -> {
                        false
                    }
                }

            }
        }
    }

}