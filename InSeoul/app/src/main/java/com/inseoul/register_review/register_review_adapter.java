package com.inseoul.register_review;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.inseoul.R;

import java.util.ArrayList;

public class register_review_adapter extends RecyclerView.Adapter<register_review_adapter.CustomViewHolder>{

    private ArrayList<register_review_recyclerview> mList;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView category;
        protected  TextView score;

        public CustomViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.place_name);
            this.category = (TextView) view.findViewById(R.id.place_category);
            this.score = (TextView) view.findViewById(R.id.place_score);
        }


    }

    public register_review_adapter(ArrayList<register_review_recyclerview> list) {
        this.mList = list;
    }


    // RecyclerView에 새로운 데이터를 보여주기 위해 필요한 ViewHolder를 생성해야 할 때 호출
    @Override
    public register_review_adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list_review, viewGroup, false);

        register_review_adapter.CustomViewHolder viewHolder = new register_review_adapter.CustomViewHolder(view);

        return viewHolder;
    }

    // Adapter의 특정 위치(position)에 있는 데이터를 보여줘야 할때 호출
    @Override
    public void onBindViewHolder(@NonNull register_review_adapter.CustomViewHolder viewholder, int position) {
        viewholder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.category.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.score.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        viewholder.name.setGravity(Gravity.CENTER);
        viewholder.category.setGravity(Gravity.CENTER);
        viewholder.score.setGravity(Gravity.CENTER);

        viewholder.name.setText(mList.get(position).getPlaceName());
        viewholder.category.setText(mList.get(position).getPlaceCategory());
        viewholder.score.setText(mList.get(position).getPlaceScore());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
