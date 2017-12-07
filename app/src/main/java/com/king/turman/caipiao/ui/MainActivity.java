package com.king.turman.caipiao.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.king.turman.caipiao.R;
import com.king.turman.caipiao.net.NetRequest;
import com.king.turman.caipiao.net.bean.LotteryBean;
import com.king.turman.caipiao.net.utils.HtmlUtil;
import com.king.turman.caipiao.ui.adapter.RefreshAdapter;
import com.king.turman.caipiao.ui.adapter.base.HeaderViewRecyclerAdapter;
import com.king.turman.caipiao.ui.listener.EndlessRecyclerOnScrollListener;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends RxAppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout  swipeRefreshLayout;
    private HeaderViewRecyclerAdapter stringAdapter;
    private List<LotteryBean> datas = new ArrayList<>();

    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.data_list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.google_blue,
                R.color.google_green,
                R.color.google_red,
                R.color.google_yellow
        );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        RefreshAdapter refreshAdapter = new RefreshAdapter(datas, this);
        stringAdapter = new HeaderViewRecyclerAdapter(refreshAdapter);
        stringAdapter.addFooterView(
                LayoutInflater
                .from(MainActivity.this)
                .inflate(R.layout.view_load_more, recyclerView, false));

        recyclerView.setAdapter(stringAdapter);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            currentPage = 1;
            requestData();
        });

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                requestData();
            }
        });

    }

    private void requestData() {
        NetRequest.getLottery("list_"+currentPage+".html")
                .compose(this.bindToLifecycle())
                .subscribe(s -> {
                    currentPage++;
                    datas.addAll(HtmlUtil.getLotteryListFromString(s));
                    swipeRefreshLayout.setRefreshing(false);
                    stringAdapter.notifyDataSetChanged();
                });
    }
}
