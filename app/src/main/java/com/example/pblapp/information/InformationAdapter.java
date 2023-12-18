package com.example.pblapp.information;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pblapp.Information;
import com.example.pblapp.R;

import java.util.ArrayList;

public class InformationAdapter extends BaseAdapter {
    private ArrayList<Information> informationList;
    private LayoutInflater inflater;

    public InformationAdapter(Context context, ArrayList<Information> informationList) {
        this.informationList = informationList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return informationList.size();
    }

    @Override
    public Object getItem(int position) {
        return informationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.nameTextView = convertView.findViewById(R.id.nameTextView);
            holder.amountTextView = convertView.findViewById(R.id.amountTextView);
            holder.dateTextView = convertView.findViewById(R.id.dateTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Information information = informationList.get(position);
        holder.nameTextView.setText(information.getNameOfMember());
        holder.amountTextView.setText(information.getCollected_amount());
        holder.dateTextView.setText(information.getCollected_date());

        return convertView;
    }

    private static class ViewHolder {
        TextView nameTextView;
        TextView amountTextView;
        TextView dateTextView;
    }
}