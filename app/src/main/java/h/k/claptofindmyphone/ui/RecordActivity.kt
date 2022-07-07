package h.k.claptofindmyphone.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import h.k.claptofindmyphone.R
import h.k.claptofindmyphone.adapter.RecordAdapter
import h.k.claptofindmyphone.databinding.ActivityRecordBinding
import h.k.claptofindmyphone.models.RecordModel
import java.util.*
import kotlin.collections.ArrayList

class RecordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecordBinding
    lateinit var sharedPeref: SharedPreferences
    lateinit var sharedPerefEditor: SharedPreferences.Editor
    lateinit var gson: Gson
    private lateinit var adaptr:RecordAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gson = Gson()
        sharedPeref = this.getSharedPreferences(
            "ctfmf", Context.MODE_PRIVATE
        )
        sharedPerefEditor = sharedPeref.edit()

        binding.historyRecyclerview.layoutManager=LinearLayoutManager(this)
        var dataA = sharedPeref.getString("record", null)
        var dataArray: ArrayList<RecordModel>? =
            gson.fromJson(dataA, object : TypeToken<ArrayList<RecordModel>>() {}.type)
        if (dataArray!=null){
            Log.e("ctag","da "+dataArray.size)
        binding.animationView.visibility=if (dataArray.size>0) View.GONE else View.VISIBLE
        }
        adaptr=RecordAdapter(this,dataArray)
        binding.historyRecyclerview.adapter=adaptr
        binding.delete.setOnClickListener {
            var dataArray = java.util.ArrayList<RecordModel>()
            dataArray.clear()
            sharedPerefEditor.putString("record", gson.toJson(dataArray))
            sharedPerefEditor.apply()
            adaptr.notifyMe(dataArray)
            binding.animationView.visibility=View.VISIBLE

        }
        binding.bak.setOnClickListener {
            finish()
        }
    }
}