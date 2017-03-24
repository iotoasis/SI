package com.canonical.democlient;

import android.app.Activity;
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
 * Created by seung-wanmun on 2016. 8. 18..
 */
public class CustomResListAdapter extends BaseAdapter {
    private final static String TAG = DiscoveryResListActivity.class.getSimpleName();

    private ArrayList<FoundDeviceInfo.ResourceInfo> listData;

    private LayoutInflater layoutInflater;
    private Context context;

    public CustomResListAdapter(Context context, ArrayList<FoundDeviceInfo.ResourceInfo> listData) {
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
        ResViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_res_layout, null);
            holder = new ResViewHolder();
            holder.ressUriView = (TextView) convertView.findViewById(R.id.dis_row_resource_uri);
            holder.resTypeView = (TextView) convertView.findViewById(R.id.dis_row_resource_type);

            convertView.setTag(holder);
        } else {
            holder = (ResViewHolder) convertView.getTag();
        }

        final String hostUri =  ((Activity)parent.getContext()).getIntent().getStringExtra("host");
        holder.ressUriView.setText(listData.get(position).resourceUri);
        String resourceTypeLine = "";
        for(String resourceType : listData.get(position).resourceTypes) {
            resourceTypeLine += resourceType;
            resourceTypeLine += ", ";
        }

        holder.resTypeView.setText(resourceTypeLine.substring(0,resourceTypeLine.length() - 2));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Trace*********************************0");
                TextView tv = (TextView) v.findViewById(R.id.dis_row_resource_uri);

                Intent intent= new Intent(context.getApplicationContext(), DetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent.putExtra("resuri", tv.getText());
                intent.putExtra("host", hostUri);
                context.getApplicationContext().startActivity(intent);
            }
        });

        return convertView;
    }

    static class ResViewHolder {
        TextView ressUriView;
        TextView resTypeView;
    }
}
