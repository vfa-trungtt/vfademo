package com.asai24.golf.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import com.asai24.golf.domain.HistoryObj;
import com.asai24.golf.inputscore.R;
import com.asai24.golf.utils.YgoLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by huynq on 9/14/16.
 */
public class AdapterFragmentHistory extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<HistoryObj> items = new ArrayList<HistoryObj>();
    private Bitmap mAgencyBitmap;
    private Bitmap mLogoBitmap;
    private Bitmap mLiveBitmap;
    private Context context;
    private LoadMoreListener loadMoreListener;
    public static enum TYPE_HISTORY{
        HEADER,
        ITEMS,
        FOOTER
    }

    public AdapterFragmentHistory(Context context, ArrayList<HistoryObj> lstHistory, Bitmap m_LogoBitmap, Bitmap m_LiveBitmap, Bitmap m_AgencyBitmap, LoadMoreListener m_loadMoreListener) {
        this.items = lstHistory;
        this.context = context;
        this.mLogoBitmap = m_LogoBitmap;
        this.mLiveBitmap = m_LiveBitmap;
        this.mAgencyBitmap = m_AgencyBitmap;
        this.loadMoreListener = m_loadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        if(items.get(position).getViewType() == TYPE_HISTORY.HEADER) {
            return TYPE_HISTORY.HEADER.ordinal();
        }else if(items.get(position).getViewType() == TYPE_HISTORY.FOOTER){
            return TYPE_HISTORY.FOOTER.ordinal();
        }else {
            return TYPE_HISTORY.ITEMS.ordinal();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == TYPE_HISTORY.HEADER.ordinal()){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_score_gift_history, parent, false);
            return new GiftHolder(view);
        }else if(viewType == TYPE_HISTORY.FOOTER.ordinal()){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.history_more_item, parent, false);
            return new FooterHolder(view);
        }else {
             view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.play_history_item, parent, false);
            return new RoundHistoryHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        YgoLog.d("TAG", "onBindViewHolder: "+String.valueOf(items.size()));
        if(items !=null && items.size() >0)
            if(items.get(position).getViewType() == TYPE_HISTORY.HEADER){
                ((GiftHolder)holder).onBindingView(items.get(position));
            }else if(items.get(position).getViewType() == TYPE_HISTORY.FOOTER){
                ((FooterHolder)holder).onBindingView(items.get(position));
            }else {
                ((RoundHistoryHolder) holder).onBindingView(items.get(position));
            }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addHeader(HistoryObj historyObj){
        if(items != null && items.size() >0) {
            if (items.get(0).getViewType() != TYPE_HISTORY.HEADER) {
                historyObj.setViewType(TYPE_HISTORY.HEADER);
                items.add(0, historyObj);
                this.notifyDataSetChanged();
            }
        }else{
            items.add(0, historyObj);
            this.notifyDataSetChanged();
        }
    }
    public void removeHeader(){
        if(items != null && items.size() >0) {
            if (items.get(0).getViewType() == TYPE_HISTORY.HEADER) {
                items.remove(0);
                this.notifyDataSetChanged();
            }
        }
    }
    public void addFooter(HistoryObj historyObj){
        int index = items.size() -1;
        if(items != null && items.size() > 0)
        if(items.get(index).getViewType() == TYPE_HISTORY.ITEMS){
            historyObj.setViewType(TYPE_HISTORY.FOOTER);
            items.add(index+1,historyObj);
            this.notifyDataSetChanged();
        }
    }
    public void removeFooter(){
        int index = items.size() -1;
        if(items != null && items.size() >0) {
            if (items.get(index).getViewType() == TYPE_HISTORY.FOOTER) {
                items.remove(index);
                this.notifyDataSetChanged();
            }
        }
    }
    public void addItems(ArrayList itemList){
        items.addAll(itemList);
        this.notifyDataSetChanged();
    }
    public void clear(){
        items = new ArrayList<>();
        this.notifyDataSetChanged();
    }
    public boolean isFooterView(){
        if(items.get(items.size() -1).getViewType() == TYPE_HISTORY.FOOTER){
            return true;
        }else{
            return false;
        }
    }

    class RoundHistoryHolder extends RecyclerView.ViewHolder {
        TextView totalScoreView;
        TextView totalPuttView;
        TextView playDateView;
        TextView clubNameView;

        ImageView upGoraView;
        ImageView liveLogoView;
        ImageView agencyIconView;
        View viewMain;

        public RoundHistoryHolder(View convertView) {
            super(convertView);
            this.totalScoreView = (TextView) convertView.findViewById(R.id.tvScoreSum);
            this.totalPuttView = (TextView) convertView.findViewById(R.id.tvPut);
            this.playDateView = (TextView) convertView.findViewById(R.id.play_date);
            this.clubNameView = (TextView) convertView.findViewById(R.id.course_name);

            this.upGoraView = (ImageView) convertView.findViewById(R.id.gora_up);
            this.liveLogoView = (ImageView) convertView.findViewById(R.id.live_label);
            this.agencyIconView = (ImageView) convertView.findViewById(R.id.agency_icon);
            viewMain = convertView;

        }
        private void onBindingView(final HistoryObj item){
            viewMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadMoreListener.onItemClickListener(item);
                }
            });
            SimpleDateFormat format = new SimpleDateFormat(context.getString(R.string.date_format1));
            totalScoreView.setText(item.getTotalShot() + "");
            totalPuttView.setText(item.getTotalPuttText());
            playDateView.setText(format.format(item.getPlayDate()));
            clubNameView.setText(item.getClubName());
            if (isValidId(item.getGora_score_id())) {
                // goraID is valid, show rakuten icon
                upGoraView.setVisibility(View.VISIBLE);
              //  upGoraView.setImageBitmap(mLogoBitmap);
            } else {
                upGoraView.setVisibility(View.GONE);
            }
            if (isValidId(item.getLiveId())) {
                // liveID is valid, show live icon
                liveLogoView.setVisibility(View.VISIBLE);
               // liveLogoView.setImageBitmap(mLiveBitmap);
            } else {
                liveLogoView.setVisibility(View.GONE);
            }
            if (isValidId(item.getAgencyRequestId())) {
                // agencyRequestID is valid, show agency icon
                agencyIconView.setVisibility(View.VISIBLE);
                agencyIconView.setImageBitmap(mAgencyBitmap);

                totalScoreView.setText("-");
                totalPuttView.setText("-");
            } else {
                agencyIconView.setVisibility(View.GONE);
            }
        }
        private boolean isValidId(String id) {
            if (id != null && !id.equals("null") && id.length() > 0) {
                return true;
            } else {
                return false;
            }
        }

    }
    class GiftHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textMessage;
        private LinearLayout giftItem;
        private ArrayList<HashMap<String, String>> hashResult;
        public GiftHolder(View itemView) {
            super(itemView);
            textMessage = (TextView) itemView.findViewById(R.id.message_receive_gift_tv);
             giftItem = (LinearLayout) itemView.findViewById(R.id.gift_item_inner_layout);

        }
        public void onBindingView(HistoryObj obj){
            giftItem.setOnClickListener(this);
            textMessage.setText(obj.getMessageGift());
            this.hashResult = obj.getHashResult();
        }

        @Override
        public void onClick(View v) {
//            switch (v.getId()){
//                case R.id.gift_item_inner_layout:
//                    Intent intent = new Intent(context, ReceiveGift.class);
//                    intent.putExtra("list_sender", hashResult);
//                    context.startActivity(intent);
//                    break;
//            }
        }
    }
    public class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtMoreView;
        View view;
        HistoryObj historyObj;
        public FooterHolder(View itemView) {
            super(itemView);
            txtMoreView = (TextView) itemView.findViewById(R.id.tvHistoryMore);
            txtMoreView.setText(R.string.more_results);
            txtMoreView.setTextColor(context.getResources().getColor(R.color.black));
            view = itemView.findViewById(R.id.rl_more_history);
        }
        public void onBindingView(HistoryObj ob){
            view.setOnClickListener(this);
            historyObj = ob;
        }

        @Override
        public void onClick(View v) {
//            switch (v.getId()){
//                case R.id.rl_more_history:
//                    loadMoreListener.onLoadMoreHistory(historyObj.getNextPage());
//                    break;
//            }
        }
    }
    public interface LoadMoreListener {
        void onLoadMoreHistory(int nextPage);
        void onItemClickListener (HistoryObj ob);

    }
}
