package com.king.turman.caipiao.ui;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

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
    private FrameLayout footLayout;

    private PopupMenu popupMenu;
    private Menu menu;

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
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        RefreshAdapter refreshAdapter = new RefreshAdapter(datas, this);
        stringAdapter = new HeaderViewRecyclerAdapter(refreshAdapter);
        footLayout = (FrameLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.view_load_more, recyclerView, false);
        stringAdapter.addFooterView(footLayout);

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

        ImageView toolImv = (ImageView) findViewById(R.id.tool_btn);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(toolImv, "rotation", 90f);
        animator1.setDuration(500);

        popupMenu = new PopupMenu(this, toolImv);
        menu = popupMenu.getMenu();

        toolImv.setOnClickListener(v-> {
            animator1.start();
            popupMenu.show();
        });
        popupMenu.setOnDismissListener(menu1 -> animator1.reverse());

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.popupmenu, menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.blue_time:
                    Intent intent = new Intent(this,BlueBallActivity.class);
                    startActivity(intent);
                    break;
                case R.id.num_times:
                    Toast.makeText(MainActivity.this, "位次频率", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            return false;
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
                },throwable -> {
                    throwable.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
//                    Toast.makeText(MainActivity.this,"没有更多数据了",Toast.LENGTH_SHORT).show();
                    footLayout.findViewById(R.id.load_progress_bar).setVisibility(View.GONE);
                    footLayout.findViewById(R.id.no_more_data).setVisibility(View.VISIBLE);
                });
    }
}
