package h.k.claptofindmyphone  .adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import h.k.claptofindmyphone.R
import h.k.claptofindmyphone.models.RecordModel

class RecordAdapter (var context: Context, var widgetList: ArrayList<RecordModel>?) : RecyclerView.Adapter<RecordAdapter.myVieholder>() {
    class myVieholder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myVieholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return myVieholder(v)
    }

    override fun onBindViewHolder(holder: myVieholder, position: Int) {
       holder.itemView.findViewById<TextView>(R.id.title).text= widgetList?.get(position)?.type ?:""
       holder.itemView.findViewById<TextView>(R.id.dat).text= widgetList?.get(position)?.date ?:""
       holder.itemView.findViewById<TextView>(R.id.tim).text= widgetList?.get(position)?.time ?: ""
    }

    fun notifyMe(list:ArrayList<RecordModel>?){
        this.widgetList=list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return widgetList?.size?:0
    }

}