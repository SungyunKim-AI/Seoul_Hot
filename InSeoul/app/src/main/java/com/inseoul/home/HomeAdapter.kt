package com.inseoul.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.inseoul.R
import com.inseoul.Server.ShowPlanRegister
import com.inseoul.add_place.AddPlaceItem
import com.inseoul.my_page.MyPage_Item
import kotlinx.android.synthetic.main.activity_add_place.*
import kotlinx.android.synthetic.main.activity_add_place_main.*
import org.json.JSONObject
import org.w3c.dom.Text

class HomeAdapter(val context: Context,
                  var listener:RecyclerViewAdapterEventListener,
                  var items:ArrayList<HomeItem>
): RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    interface RecyclerViewAdapterEventListener {
        fun onClick(view: View, position:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home, parent, false)
        return ViewHolder(v)
    }

    fun getData(position: Int): HomeItem? {
        return if (items == null || items.size < position) {
            null
        } else items[position]

    }

    override fun getItemCount(): Int {
        if(items==null)
            return 0
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items!!.get(position)

        val imgName = data.thumbnail.split(",")
//        holder.thumbnail.setImageResource(R.drawable.sample2)
        val url = "http://ksun1234.cafe24.com/" + imgName[0]
        Log.d("thumbnail url", url)
        Glide.with(context).load(url).thumbnail(0.1f).placeholder(R.drawable.logo).into(holder.thumbnail)
        holder.item_title.text = data.title
        holder.item_content.text = data.content
        holder.likes.text = data.likes
//        var m = data.mem.split(",")
//        var str = ""
//        for(i in m){
//            str += i
//            str += " "
//        }
        holder.writer.text = "ⓒ"+ data.mem
        holder.heart.setOnClickListener {
            val responseListener = Response.Listener<String> { response ->

            }
            //val idnumrequest = ShowPlanRegister(id, responseListener)
            //val queue = Volley.newRequestQueue()
            //queue.add(idnumrequest)
        }



        Log.v("In RecyclerView", "Bind Item")
        holder.itemView.setOnClickListener {
            listener!!.onClick(it, position)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var thumbnail: ImageView
        var item_title: TextView
        var item_content: TextView
        var likes:TextView
        var writer:TextView
        var heart:ToggleButton
        init {
            item_title = itemView.findViewById(R.id.item_title)
            thumbnail = itemView.findViewById(R.id.item_img)
            item_content = itemView.findViewById(R.id.item_content)
            likes = itemView.findViewById(R.id.likes)
            writer = itemView.findViewById(R.id.writer)
            heart = itemView.findViewById(R.id.heart)
        }
    }



}

