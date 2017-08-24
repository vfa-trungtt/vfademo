package com.asai24.golf.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.asai24.golf.domain.HistoryObj;
import com.asai24.golf.inputscore.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by huynq on 11/25/16.
 */

public class RoundHistoryAdapter extends ArrayAdapter<HistoryObj> {

    private int resourceId;
    private List<HistoryObj> items;
    private RoundHistoryHolder holder;
    private Bitmap mAgencyBitmap;
    private Bitmap mLogoBitmap;
    private Bitmap mLiveBitmap;

    public RoundHistoryAdapter(Context context, int resourceId, List<HistoryObj> items, Bitmap mLogoBitmap, Bitmap mLiveBitmap, Bitmap mAgencyBitmap) {
        super(context, resourceId, items);
        this.resourceId = resourceId;
        this.items = items;
        this.mAgencyBitmap = mAgencyBitmap;
        this.mLiveBitmap = mLiveBitmap;
        this.mLogoBitmap = mLogoBitmap;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryObj item = items.get(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(resourceId, null);
            holder = null;
            holder = new RoundHistoryHolder();
            holder.totalScoreView = (TextView) convertView.findViewById(R.id.tvScoreSum);
            holder.totalPuttView = (TextView) convertView.findViewById(R.id.tvPut);
            holder.playDateView = (TextView) convertView.findViewById(R.id.play_date);
            holder.clubNameView = (TextView) convertView.findViewById(R.id.course_name);

            holder.upGoraView = (ImageView) convertView.findViewById(R.id.gora_up);
            holder.liveLogoView = (ImageView) convertView.findViewById(R.id.live_label);
            holder.agencyIconView = (ImageView) convertView.findViewById(R.id.agency_icon);

            convertView.setTag(holder);
        } else {
            holder = (RoundHistoryHolder) convertView.getTag();
        }

        SimpleDateFormat format = new SimpleDateFormat(getContext().getString(R.string.date_format1));

        holder.totalScoreView.setText(item.getTotalShot() + "");
        holder.totalPuttView.setText(item.getTotalPuttText());
        holder.playDateView.setText(format.format(item.getPlayDate()));
        holder.clubNameView.setText(item.getClubName());

        if (isValidId(item.getGora_score_id())) {
            // goraID is valid, show rakuten icon
            holder.upGoraView.setVisibility(View.VISIBLE);
            holder.upGoraView.setImageBitmap(mLogoBitmap);
        } else {
            holder.upGoraView.setVisibility(View.GONE);
        }
        if (isValidId(item.getLiveId())) {
            // liveID is valid, show live icon
            holder.liveLogoView.setVisibility(View.VISIBLE);
            holder.liveLogoView.setImageBitmap(mLiveBitmap);
        } else {
            holder.liveLogoView.setVisibility(View.GONE);
        }
        if (isValidId(item.getAgencyRequestId())) {
            // agencyRequestID is valid, show agency icon
            holder.agencyIconView.setVisibility(View.VISIBLE);
            holder.agencyIconView.setImageBitmap(mAgencyBitmap);

            holder.totalScoreView.setText("-");
            holder.totalPuttView.setText("-");
        } else {
            holder.agencyIconView.setVisibility(View.GONE);
        }

        return convertView;
    }

    private boolean isValidId(String id) {
        if (id != null && !id.equals("null") && id.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private class RoundHistoryHolder {
        TextView totalScoreView;
        TextView totalPuttView;
        TextView playDateView;
        TextView clubNameView;

        ImageView upGoraView;
        ImageView liveLogoView;
        ImageView agencyIconView;
    }


}
