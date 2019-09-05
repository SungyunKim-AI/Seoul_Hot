package com.inseoul.register_review

import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inseoul.R

import java.util.ArrayList

class register_review_adapter(private val mList: ArrayList<register_review_recyclerview>?) :
    RecyclerView.Adapter<register_review_adapter.CustomViewHolder>() {

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView
        var category: TextView
        var score: TextView

        init {
            this.name = view.findViewById<View>(R.id.place_name) as TextView
            this.category = view.findViewById<View>(R.id.place_category) as TextView
            this.score = view.findViewById<View>(R.id.place_score) as TextView
        }


    }


    // RecyclerView에 새로운 데이터를 보여주기 위해 필요한 ViewHolder를 생성해야 할 때 호출
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): register_review_adapter.CustomViewHolder {
        // View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_list_review, viewGroup, false)

        return CustomViewHolder(view)
    }

    // Adapter의 특정 위치(position)에 있는 데이터를 보여줘야 할때 호출
    override fun onBindViewHolder(viewholder: register_review_adapter.CustomViewHolder, position: Int) {
        viewholder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f)
        viewholder.category.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f)
        viewholder.score.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f)

        viewholder.name.gravity = Gravity.CENTER
        viewholder.category.gravity = Gravity.CENTER
        viewholder.score.gravity = Gravity.CENTER

        viewholder.name.text = mList!![position].placeName
        viewholder.category.text = mList[position].placeCategory
        viewholder.score.text = mList[position].placeScore
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }
}
