package com.example.lepton.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.lepton.PlaceAutocomplete;
import com.example.leptontest.R;

import java.util.ArrayList;

/**
 * Naresh
 */
public class PlaceAdapter extends BaseAdapter {


    public  ArrayList<PlaceAutocomplete> mList;
    LayoutInflater mLayoutInflater;

    public PlaceAdapter(ArrayList<PlaceAutocomplete> mList, Context ctx) {
        this.mList = mList;
        mLayoutInflater = LayoutInflater.from(ctx);
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View mView = convertView;
        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            mView = mLayoutInflater.inflate(R.layout.location_search_itemv2, null);
            mViewHolder.place_desc = mView.findViewById(R.id.tv_sub);
            mViewHolder.place_name = mView.findViewById(R.id.tv_title);
            mView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) mView.getTag();
        }
        mViewHolder.place_name.setText( mList.get(i).placeName);
        mViewHolder.place_desc.setText( mList.get(i).description);

        //   mViewHolder.iv_nav_icon.setImageResource(navDataModel.getIcon());

        return mView;
    }






    public class ViewHolder {

        TextView place_name,place_desc;

    }


}

