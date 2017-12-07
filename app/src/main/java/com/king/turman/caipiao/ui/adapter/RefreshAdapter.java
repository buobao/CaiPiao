package com.king.turman.caipiao.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.king.turman.caipiao.R;
import com.king.turman.caipiao.net.bean.LotteryBean;

import java.util.List;

/**
 * Created by diaoqf on 2017/12/7.
 */

public class RefreshAdapter extends RecyclerView.Adapter<RefreshAdapter.ViewHolder> {

    List<LotteryBean> mList;
    Context context;

    public RefreshAdapter(List<LotteryBean> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(mList.get(position).getNo());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_text);
        }
    }
}