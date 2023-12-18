package com.example.pblapp.information1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pblapp.Information;
import com.example.pblapp.Information1;
import com.example.pblapp.R;

import java.util.ArrayList;

public class Information1Adapter extends BaseAdapter {
    private ArrayList<Information1> informationList1;
    private LayoutInflater inflater;

    public Information1Adapter(Context context, ArrayList<Information1> informationList1) {
        this.informationList1 = informationList1;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return informationList1.size();
    }

    @Override
    public Object getItem(int position1) {
        return informationList1.get(position1);
    }

    @Override
    public long getItemId(int position1) {
        return position1;
    }

    @Override
    public View getView(int position1, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.loan_item, parent, false);
            holder = new ViewHolder();
            holder.nameTextView1 = convertView.findViewById(R.id.nameTextView1);
            holder.amountTextView1 = convertView.findViewById(R.id.amountTextView1);
            holder.dateTextView1 = convertView.findViewById(R.id.dateTextView1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Information1 information1 = informationList1.get(position1);
        holder.nameTextView1.setText(information1.getNameOfMember());
        holder.amountTextView1.setText(information1.getloanAmount());
        holder.dateTextView1.setText(information1.getloanDate());

        return convertView;
    }

    private static class ViewHolder {
        TextView nameTextView1;
        TextView amountTextView1;
        TextView dateTextView1;
    }
}