package com.example.pblapp.information2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pblapp.Information2;
import com.example.pblapp.R;

import java.util.ArrayList;

public class Information2Adapter extends BaseAdapter {
    private ArrayList<Information2> informationList2;
    private LayoutInflater inflater;

    public Information2Adapter(Context context, ArrayList<Information2> informationList2) {
        this.informationList2 = informationList2;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return informationList2.size();
    }

    @Override
    public Object getItem(int position2) {
        return informationList2.get(position2);
    }

    @Override
    public long getItemId(int position2) {
        return position2;
    }

    @Override
    public View getView(int position2, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.allrec_item, parent, false);
            holder = new ViewHolder();
            holder.nameTextView2 = convertView.findViewById(R.id.nameTextView2);
            holder.amountTextView2 = convertView.findViewById(R.id.amountTextView2);
            holder.dateTextView2 = convertView.findViewById(R.id.dateTextView2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Information2 information2 = informationList2.get(position2);
        holder.nameTextView2.setText(information2.getNameOfMember());
        holder.amountTextView2.setText(information2.getCollected_amount());
        holder.dateTextView2.setText(information2.getCollected_date());

        return convertView;
    }

    private static class ViewHolder {
        TextView nameTextView2;
        TextView amountTextView2;
        TextView dateTextView2;
    }
}
