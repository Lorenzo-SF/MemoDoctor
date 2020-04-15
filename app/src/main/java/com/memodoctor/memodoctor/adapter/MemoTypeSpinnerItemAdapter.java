package com.memodoctor.memodoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.memodoctor.memodoctor.R;
import com.memodoctor.memodoctor.model.MemoType;

import java.util.Iterator;
import java.util.List;

public class MemoTypeSpinnerItemAdapter extends BaseAdapter {
    private android.content.Context Context;
    private int Layout;
    List<MemoType> MemoTypes;

    public MemoTypeSpinnerItemAdapter(Context context, int layout, List<MemoType> memoTypes){
        this.Context = context;
        this.Layout = layout;
        this.MemoTypes = memoTypes;
    }

    @Override
    public int getCount() {
        return this.MemoTypes.size();
    }

    @Override
    public Object getItem(int position) {
        return this.MemoTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MemoType mt = this.MemoTypes. get(position);

        TypeSpinnerViewHolder viewHolder;

        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(this.Context);
            convertView = layoutInflater.inflate(R.layout.memotype_item, null);

            viewHolder = new TypeSpinnerViewHolder();
            viewHolder.ItemType = (TextView) convertView.findViewById(R.id.ItemType);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TypeSpinnerViewHolder) convertView.getTag();
        }

        viewHolder.ItemType.setText(mt.GetDescription());
        viewHolder.ItemType.setTag(mt.GetId());

        return convertView;
    }

    // Clase estatica para mejorar el performance de la navegacion de la lista
    static class TypeSpinnerViewHolder{
        private TextView ItemType;
    }
}
