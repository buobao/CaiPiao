package com.king.turman.caipiao.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.king.turman.caipiao.R;
import com.king.turman.caipiao.net.bean.LotteryBean;

import java.util.ArrayList;
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
        holder.no.setText(mList.get(position).getNo());
        holder.redNum1.setText(mList.get(position).getNumbers().get(0));
        holder.redNum2.setText(mList.get(position).getNumbers().get(1));
        holder.redNum3.setText(mList.get(position).getNumbers().get(2));
        holder.redNum4.setText(mList.get(position).getNumbers().get(3));
        holder.redNum5.setText(mList.get(position).getNumbers().get(4));
        holder.redNum6.setText(mList.get(position).getNumbers().get(5));
        holder.blueNum.setText(mList.get(position).getNumbers().get(6));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView no;
        TextView redNum1,redNum2,redNum3,redNum4,redNum5,redNum6;
        TextView blueNum;

        public ViewHolder(View itemView) {
            super(itemView);
            no = itemView.findViewById(R.id.tv_no);
            redNum1 = itemView.findViewById(R.id.read_num_1);
            redNum2 = itemView.findViewById(R.id.read_num_2);
            redNum3 = itemView.findViewById(R.id.read_num_3);
            redNum4 = itemView.findViewById(R.id.read_num_4);
            redNum5 = itemView.findViewById(R.id.read_num_5);
            redNum6 = itemView.findViewById(R.id.read_num_6);
            blueNum = itemView.findViewById(R.id.blue_num);
        }
    }
}