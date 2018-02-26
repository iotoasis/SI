package com.canonical.democlient;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by seung-wanmun on 2016. 8. 11..
 */
public class CustomListAdapter extends BaseAdapter {

    private final static String TAG = DiscoveryListActivity.class.getSimpleName();

    private ArrayList<FoundItem> listData;

    private LayoutInflater layoutInflater;
    private Context context;

    public CustomListAdapter(Context context, ArrayList<FoundItem> listData) {
        this.context = context;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }
/*
    public void add(FoundItem foundItem) {
        listData.add(foundItem);
        notifyDataSetChanged();
    }
*/
    @Override
    public int getCount() {

        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Context getContext() {
        return this.context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
            holder = new ViewHolder();
            holder.headDeviceNameView = (TextView) convertView.findViewById(R.id.dis_row_device_name);
            holder.hostUrlView = (TextView) convertView.findViewById(R.id.dis_row_host);
            holder.deviceIdView = (TextView) convertView.findViewById(R.id.dis_row_device_id);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.headDeviceNameView.setText(listData.get(position).getDeviceName());
        holder.hostUrlView.setText(listData.get(position).getHost());
        holder.deviceIdView.setText(listData.get(position).getDeviceId());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Trace*********************************0");
                TextView tv = (TextView) v.findViewById(R.id.dis_row_host);

                Intent intent= new Intent(context.getApplicationContext(), DiscoveryResListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent.putExtra("host", tv.getText());
                context.getApplicationContext().startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView headDeviceNameView;
        TextView hostUrlView;
        TextView deviceIdView;
    }
}
