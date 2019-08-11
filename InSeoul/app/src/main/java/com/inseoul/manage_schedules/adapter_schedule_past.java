package com.inseoul.manage_schedules;

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

public class adapter_schedule_past extends RecyclerView.Adapter<adapter_schedule_past.CustomViewHolder>{
    private ArrayList<recyclerview_schedule_past> mList;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView date;

        public CustomViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.textview_recyclerview_title_past);
            this.date = (TextView) view.findViewById(R.id.textview_recyclerview_date_past);
        }
    }

    public adapter_schedule_past(ArrayList<recyclerview_schedule_past> list) {
        this.mList = list;
    }


    // RecyclerView에 새로운 데이터를 보여주기 위해 필요한 ViewHolder를 생성해야 할 때 호출
    @Override
    public adapter_schedule_past.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list_past_schedules, viewGroup, false);

        adapter_schedule_past.CustomViewHolder viewHolder = new adapter_schedule_past.CustomViewHolder(view);

        return viewHolder;
    }


    // Adapter의 특정 위치(position)에 있는 데이터를 보여줘야 할때 호출
    @Override
    public void onBindViewHolder(@NonNull adapter_schedule_past.CustomViewHolder viewholder, int position) {

        viewholder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.date.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        viewholder.title.setGravity(Gravity.CENTER);
        viewholder.date.setGravity(Gravity.CENTER);

        viewholder.title.setText(mList.get(position).getSchedule_title_past());
        viewholder.date.setText(mList.get(position).getSchedule_date_past());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
