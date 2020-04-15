package com.memodoctor.memodoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.memodoctor.memodoctor.R;
import com.memodoctor.memodoctor.model.MemoTask;
import com.memodoctor.memodoctor.model.MemoType;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class MemoTaskListItemAdapter extends BaseAdapter {
    private Context Context;
    private int Layout;
    List<MemoTask> MemoTasks;

    public MemoTaskListItemAdapter(Context context, int layout, List<MemoTask> memoTasks){
        this.Context = context;
        this.Layout = layout;
        this.MemoTasks = memoTasks;
    }

    @Override
    public int getCount() {
        return this.MemoTasks.size();
    }

    @Override
    public Object getItem(int position) {
        return this.MemoTasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return MemoTasks.get(position).GetId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MemoTask mt = this.MemoTasks.get(position);
        MemoTaskViewHolder viewHolder;

        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(this.Context);
            convertView = layoutInflater.inflate(R.layout.memotask_item, null);

            viewHolder = new MemoTaskViewHolder();
            viewHolder.MemoTypeDescription = (TextView) convertView.findViewById(R.id.memoTypeDescription);
            viewHolder.MemoTaskDate = (TextView) convertView.findViewById(R.id.memoTaskDate);
            viewHolder.MemoTypeImage  = (ImageView) convertView.findViewById(R.id.MemoTypeImage);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MemoTaskViewHolder) convertView.getTag();
        }

        viewHolder.MemoTypeDescription.setText(mt.GetMemoType().GetDescription());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
        viewHolder.MemoTaskDate.setText(sdf.format(mt.GetMemoDate()));

        return convertView;
    }

    // Clase estatica para mejorar el performance de la navegacion de la lista
    static class MemoTaskViewHolder{
        private TextView MemoTypeDescription;
        private TextView MemoTaskDate;
        private ImageView MemoTypeImage;
    }
}
